package uk.co.whitewillow.minesweepertv

import android.util.Log
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

enum class GameState {
    STOPPED, PLAYING, WON, LOST
}

data class MineFieldConfig(
    val width: Int,
    val height: Int,
    val count: Int
)

data class MineField(
    val gridMines: List<MineRow>,
    val gridConfig: MineFieldConfig,
    val gameState: GameState
) {
    companion object Factory {
        fun create(config: MineFieldConfig): MineField {
            val mineFieldList = List(config.height) { MineRow(List(config.width) { MineSquare(false) }) }
            var minesToAllocate = config.count
            while (minesToAllocate > 0) {
                val mineCellIndex = Random.nextInt(0, config.width*config.height)
                val x = mineCellIndex % config.width
                val y = mineCellIndex / config.width
                if (!mineFieldList[y].rowMines[x].minePresent) {
                    mineFieldList[y].rowMines[x].minePresent = true
                    minesToAllocate--
                }
            }

            return MineField(mineFieldList, config, GameState.STOPPED)
        }
    }

    init {
        // Set neighbourCount on first construction only for each mine as this will never change
        if (gridMines[0].rowMines[0].neighbourCount == -1) {
            gridMines.forEachIndexed { y, mineRow ->
                mineRow.rowMines.forEachIndexed { x, mineSquare ->
                    mineSquare.neighbourCount = mineRow.getLeftRightMineCount(x)
                    if (y > 0) mineSquare.neighbourCount += gridMines[y-1].getLeftRightMineCount(x)
                    if (y+1 < gridMines.size) mineSquare.neighbourCount += gridMines[y+1].getLeftRightMineCount(x)
                    //Log.d("MineField","Index $x count now ${mineSquare.neighbourCount}")
                }
            }
        }
    }

    fun visibleClicked(clickedSquare: MineSquare): MineField {
        return if (clickedSquare.visible || clickedSquare.flagMarked ||
            gameState == GameState.WON || gameState == GameState.LOST) {
            // Click is invalid for some reason, ignore it
            this
        } else {
            setVisible(clickedSquare)
        }
    }

    private fun setVisible(clickedSquare: MineSquare): MineField {
        if (clickedSquare.neighbourCount == 0) {
            sweep(clickedSquare)
        }

        val newGridMines = gridMines.map { mineRow ->
            // Call row setVisible for all rows if sweeping, otherwise just for the row containing the clicked square
            if ((clickedSquare.neighbourCount == 0) || mineRow.rowMines.contains(clickedSquare)) mineRow.setVisible(clickedSquare)
            else mineRow
        }
        val newGameState = if (clickedSquare.minePresent) GameState.LOST
            else if (visibleCount(newGridMines) == gridConfig.width * gridConfig.height - gridConfig.count) GameState.WON
            else GameState.PLAYING

        return copy(gridMines = newGridMines, gameState = newGameState)
    }

    private fun sweep(mineSquare: MineSquare) {
        Log.d("MineField", "Sweeping")
        var foundX = -1
        var foundY = -1
        rowLoop@ for (y in gridMines.indices) {
            if (gridMines[y].rowMines.contains(mineSquare)) {
                foundX = gridMines[y].rowMines.indexOf(mineSquare)
                foundY = y
                break@rowLoop
            }
        }
        sweepMark(foundX, foundY)
    }

    private fun sweepMark(x: Int, y: Int) {
        val mineSquare = gridMines[y].rowMines[x]
        if (mineSquare.sweepMarked) return
        mineSquare.sweepMarked = true

        if (mineSquare.neighbourCount == 0) {
            // Go round each of the 8 surrounding squares and call sweepMark
            for (yy in max(y-1,0) .. min(y+1,gridMines.size-1)) {
                for (xx in max(x-1,0) .. min(x+1,gridMines[yy].rowMines.size-1)) {
                    if ((xx == x) && (yy == y)) {} // already sweeping this square
                    else sweepMark(xx, yy)
                }
            }
        }
    }

    fun flagClicked(clickedSquare: MineSquare): MineField {
        return if (clickedSquare.visible || gameState != GameState.PLAYING) {
            // Click is invalid for some reason, ignore it
            this
        } else {
            flagToggle(clickedSquare)
        }
    }

    private fun flagToggle(clickedSquare: MineSquare): MineField {
        return copy(gridMines = gridMines.map { mineRow ->
            if (mineRow.rowMines.contains(clickedSquare)) mineRow.flagToggle(clickedSquare)
            else mineRow
        })
    }

    private fun visibleCount(_gridMines: List<MineRow>): Int {
        // Operates on provided gridMines (the new state) rather than the class variable (the old state)
        return _gridMines.sumOf {
                mineRow -> mineRow.rowMines.sumOf {
                mineSquare -> if (mineSquare.visible) 1.toInt() else 0
            }
        }
    }

    private fun flagMarkedCount(): Int {
        return gridMines.sumOf {
            mineRow -> mineRow.rowMines.sumOf {
                mineSquare -> if (mineSquare.flagMarked) 1.toInt() else 0
            }
        }
    }

    fun flagRemainingCount(): Int {
        return gridConfig.count - flagMarkedCount()
    }
}