package uk.co.whitewillow.minesweepertv

data class MineRow(
    val rowMines: List<MineSquare>
) {
    fun getLeftRightMineCount(x: Int): Int {
        var mineCount = 0
        if ((x > 0) && (rowMines[x-1].minePresent)) mineCount++
        if (rowMines[x].minePresent) mineCount++
        if ((x+1 < rowMines.size) && (rowMines[x+1].minePresent)) mineCount++
        return mineCount
    }

    fun setVisible(clickedSquare: MineSquare): MineRow {
        return copy(rowMines = rowMines.map { mineSquare ->
            if ((mineSquare == clickedSquare) || mineSquare.sweepMarked) {
                mineSquare.copy(visible = true, sweepMarked = false)
            }
            else mineSquare
        })
    }

    fun flagToggle(clickedSquare: MineSquare): MineRow {
        return copy(rowMines = rowMines.map { mineSquare ->
            if (mineSquare == clickedSquare) {
                mineSquare.copy(flagMarked = !mineSquare.flagMarked)
            }
            else mineSquare
        })
    }
}