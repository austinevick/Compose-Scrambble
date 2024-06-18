package com.example.scrramble.ui

data class GameUIState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)

sealed class WordResponseState {
    data object None : WordResponseState()
    data object IsLoading : WordResponseState()
    data class Error(val message: String) : WordResponseState()
    data class Success(val data: String) : WordResponseState()
}