package uk.co.whitewillow.minesweepertv

import org.junit.Test
import org.junit.Assert.*

class MinesweeperTvUnitTest {
    @Test
    fun testNeighbourCount() {
        val mf = MineField(
            listOf(
                MineRow(listOf(MineSquare(true),MineSquare(true),MineSquare(true))),
                MineRow(listOf(MineSquare(true),MineSquare(false),MineSquare(true))),
                MineRow(listOf(MineSquare(true),MineSquare(true),MineSquare(true)))
            ),
            MineFieldConfig(3, 3, 8),
            GameState.STOPPED
        )
        val middleSquare = mf.gridMines[1].rowMines[1]
        assertEquals(8, middleSquare.neighbourCount)
    }

    @Test
    fun testEmptyMinefield() {
        val mf = MineField.create(MineFieldConfig(3, 3, 0))
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                assertFalse(mf.gridMines[y].rowMines[x].minePresent)
            }
        }
    }

    @Test
    fun testPartialMinefield() {
        val mf = MineField.create(MineFieldConfig(3, 3, 5))
        var mineCount = 0
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                if (mf.gridMines[y].rowMines[x].minePresent) {
                    println("Mine at x=$x y=$y")
                    mineCount++
                }
            }
        }
        assertEquals(mineCount, 5)
    }

    @Test
    fun testFullMinefield() {
        val mf = MineField.create(MineFieldConfig(3, 3, 9))
        var mineCount = 0
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                if (mf.gridMines[y].rowMines[x].minePresent) {
                    println("Mine at x=$x y=$y")
                    mineCount++
                }
            }
        }
        assertEquals(mineCount, 9)
    }
}