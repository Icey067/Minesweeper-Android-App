package com.example.miner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
                // Use a dark, modern background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF121212) // Dark Charcoal
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
    var gameStatus by remember { mutableStateOf("Playing") } // "Playing", "Lost", "Won"
    val gridSize = 9
    val bombCount = 2

    fun resetGame() {
        bombLocations = (0 until gridSize).shuffled().take(bombCount).toSet()
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
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF39FF14), // Neon Green
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ThreeByThreeGrid(
            gridSize = gridSize,
            bombLocations = bombLocations,
            revealedButtons = revealedButtons,
            gameStatus = gameStatus,
            onButtonClick = { index ->
                if (gameStatus == "Playing" && index !in revealedButtons) {
                    val newRevealed = revealedButtons + index
                    if (bombLocations.contains(index)) {
                        gameStatus = "Lost"
                        revealedButtons = newRevealed // Show the bomb they clicked
                    } else {
                        revealedButtons = newRevealed
                        // Check for win condition
                        val nonBombCells = (0 until gridSize).toSet() - bombLocations
                        if (newRevealed.containsAll(nonBombCells)) {
                            gameStatus = "Won"
                        }
                    }
                }
            }
        )

        // Show game over messages and reset button
        if (gameStatus == "Lost") {
            GameStatusText(text = "You Lost!", color = Color(0xFFFF4444))
            PlayAgainButton(onReset = { resetGame() })
        }
        if (gameStatus == "Won") {
            GameStatusText(text = "You Won!", color = Color(0xFF39FF14))
            PlayAgainButton(onReset = { resetGame() })
        }
    }
}

@Composable
fun ThreeByThreeGrid(
    gridSize: Int,
    bombLocations: Set<Int>,
    revealedButtons: Set<Int>,
    gameStatus: String,
    onButtonClick: (Int) -> Unit
) {
    // A distinct surface for the game board
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        color = Color(0xFF1E1E1E), // Slightly lighter than background
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF333333))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp) // Padding inside the game board
        ) {
            items(gridSize) { index ->
                val isGameOver = gameStatus != "Playing"
                val isRevealed = revealedButtons.contains(index)
                val isBomb = bombLocations.contains(index)

                // Define colors
                val hiddenColor = Color(0xFF4A4A4A)
                val revealedSafeColor = Color(0xFF0096FF) // A bright, eye-catching blue
                val bombColor = Color(0xFFFF4444)

                // Determine the correct color and text based on game state
                val (color, text) = when {
                    // Case 1: Button is revealed
                    isRevealed -> {
                        if (isBomb) (bombColor to "💣") // This reveal lost the game
                        else (revealedSafeColor to "💎") // This is a safe reveal (with diamond)
                    }
                    // Case 2: Button not revealed, but game is over
                    isGameOver -> {
                        if (isBomb) (bombColor.copy(alpha = 0.7f) to "💣") // Show un-revealed bombs
                        else (hiddenColor.copy(alpha = 0.5f) to "") // Show un-revealed safe
                    }
                    // Case 3: Button not revealed, game is playing
                    else -> {
                        (hiddenColor to "")
                    }
                }

                Button(
                    onClick = { onButtonClick(index) },
                    modifier = Modifier.aspectRatio(1f),
                    enabled = !isRevealed && !isGameOver,
                    shape = RoundedCornerShape(12.dp), // Nicer rounded corners
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 1.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = hiddenColor, // Always show this when enabled
                        disabledContainerColor = color // Show the calculated color when disabled
                    )
                ) {
                    if (text.isNotEmpty()) {
                        Text(text = text, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun GameStatusText(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp)
    )
}

@Composable
fun PlayAgainButton(onReset: () -> Unit) {
    Button(
        onClick = onReset,
        modifier = Modifier.padding(top = 16.dp),
        shape = RoundedCornerShape(50), // Pill shape
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF39FF14) // Neon green
        )
    ) {
        Text(
            text = "Play Again",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MinerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF121212)
        ) {
            MinesweeperScreen()
        }
    }
}

