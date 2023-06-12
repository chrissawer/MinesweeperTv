package uk.co.whitewillow.minesweepertv

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val easy = MineFieldConfig(8, 8, 8)
private val medium = MineFieldConfig(16, 16, 40)
private var mineField by mutableStateOf(MineField.create(easy))
private var flagMarkMode by mutableStateOf(false)

@Composable
fun MinesweeperTvApp() {
    Column {
        CurrentGameState()
        SecondsCount()
        FlagRemainingCount()
        Switch(checked = flagMarkMode, onCheckedChange = { markMode_ -> flagMarkMode = markMode_ })
        for (mineRow in mineField.gridMines) {
            Row {
                for (mine in mineRow.rowMines) {
                    MineCell(mine)
                }
            }
        }
    }
}

@Composable
fun MineCell(mineSquare: MineSquare, minesweeperTvViewModel: MinesweeperTvViewModel = viewModel()) {
    Text(text = mineSquare.toString(),
        modifier = Modifier.clickable {
            if (flagMarkMode) {
                Log.d("MinesweeperTvApp", "Click - flagClicked")
                mineField = mineField.flagClicked(mineSquare)
            } else {
                Log.d("MinesweeperTvApp", "Click - visibleClicked")
                val oldGameState = mineField.gameState
                mineField = mineField.visibleClicked(mineSquare)

                if (mineField.gameState != oldGameState) {
                    if (mineField.gameState == GameState.PLAYING) {
                        Log.d("MinesweeperTvApp", "Game started")
                        minesweeperTvViewModel.startTimer()
                    } else {
                        minesweeperTvViewModel.stopTimer()
                        Log.d("MinesweeperTvApp", "Game completed")
                    }
                }
            }
        }.width(20.dp).background(mineSquare.getBackgroundColour()),
        textAlign = TextAlign.Center,
        color = mineSquare.getColour(),
    )
}

@Composable
fun FlagRemainingCount() {
    Text(text = mineField.flagRemainingCount().toString())
}

@Composable
fun SecondsCount(minesweeperTvViewModel: MinesweeperTvViewModel = viewModel()) {
    Text(text = minesweeperTvViewModel.seconds.value.toString())
}

@Composable
fun CurrentGameState(minesweeperTvViewModel: MinesweeperTvViewModel = viewModel()) {
    Text(text = mineField.gameState.toString(),
        modifier = Modifier.clickable {
            minesweeperTvViewModel.resetTime()
            mineField = MineField.create(easy)
        })
}