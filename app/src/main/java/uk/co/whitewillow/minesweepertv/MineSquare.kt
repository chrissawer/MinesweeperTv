package uk.co.whitewillow.minesweepertv

import androidx.compose.ui.graphics.Color

data class MineSquare(
    var minePresent: Boolean,
    var visible: Boolean = false,
    var neighbourCount: Int = -1,
    var sweepMarked: Boolean = false,
    var flagMarked: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun toString(): String {
        return if (!visible) {
            if (flagMarked) "F"
            else ""
        }
        else {
            if (minePresent) "M"
            else if (neighbourCount == 0) ""
            else neighbourCount.toString()
        }
    }

    fun getColour(): Color {
        return if (!visible) Color.Red
        else if (minePresent) Color.Black
        else when (neighbourCount) {
            3 -> Color.Red
            2 -> Color.Green
            1 -> Color.Blue
            else -> Color.Black
        }
    }

    fun getBackgroundColour(): Color {
        return if (!visible) Color.DarkGray
        else Color.Gray
    }
}