package com.example.miner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miner.ui.theme.MinerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    MinesweeperScreen()
                }
            }
        }
    }
}

@Composable
fun MinesweeperScreen() {
    var bombLocations by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var revealedButtons by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var gameStatus by remember { mutableStateOf("Playing") } // "Playing", "Lost"

    fun resetGame() {
        bombLocations = (0..8).shuffled().take(2).toSet()
        revealedButtons = emptySet()
        gameStatus = "Playing"
    }

    LaunchedEffect(Unit) {
        resetGame()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mine Sweeper",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ThreeByThreeGrid(
            bombLocations = bombLocations,
            revealedButtons = revealedButtons,
            gameStatus = gameStatus,
            onButtonClick = { index ->
                if (gameStatus == "Playing") {
                    revealedButtons = revealedButtons + index
                    if (bombLocations.contains(index)) {
                        gameStatus = "Lost"
                    }
                }
            }
        )

        if (gameStatus == "Lost") {
            Text(
                text = "You Lost!",
                color = Color.Red,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
            Button(
                onClick = { resetGame() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Play Again")
            }
        }
    }
}

@Composable
fun ThreeByThreeGrid(
    bombLocations: Set<Int>,
    revealedButtons: Set<Int>,
    gameStatus: String,
    onButtonClick: (Int) -> Unit
) {
    Box(modifier = Modifier.padding(horizontal = 30.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(9) { index ->
                val isRevealed = revealedButtons.contains(index)
                val isBomb = bombLocations.contains(index)
                val isGameOver = gameStatus != "Playing"

                Button(
                    onClick = { onButtonClick(index) },
                    modifier = Modifier.aspectRatio(1f),
                    enabled = !isRevealed && !isGameOver,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRevealed && isBomb) Color.Red else MaterialTheme.colorScheme.primary,
                        disabledContainerColor = when {
                            isRevealed && isBomb -> Color.Red
                            isRevealed && !isBomb -> Color.DarkGray
                            isGameOver && isBomb -> Color.Red.copy(alpha = 0.5f) // Show bombs on game over
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        }
                    )
                ) {
                    if (isRevealed || (isGameOver && isBomb)) {
                        Text(text = if (isBomb) "💣" else "✔", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MinerTheme {
        Surface(color = Color.Black) {
            MinesweeperScreen()
        }
    }
}

