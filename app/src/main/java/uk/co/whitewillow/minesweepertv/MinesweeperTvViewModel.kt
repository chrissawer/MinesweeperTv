package uk.co.whitewillow.minesweepertv

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MinesweeperTvViewModel : ViewModel() {
    var seconds: MutableState<Int> = mutableStateOf(0)

    private var timer: Timer ?= null

    fun startTimer() {
        if (timer == null) {
            timer = fixedRateTimer("ChrisTimer", false, 1000L, 1000L) {
                seconds.value++
                Log.d("MinesweeperTvViewModel", "Timer tick to ${seconds.value}")
            }
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    fun resetTime() {
        seconds.value = 0
    }
}