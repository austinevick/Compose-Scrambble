package com.example.scrramble.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scrramble.common.COMMAND
import com.example.scrramble.data.MAX_NO_OF_WORDS
import com.example.scrramble.data.SCORE_INCREASE
import com.example.scrramble.data.WordModel
import com.example.scrramble.data.allWords
import com.example.scrramble.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    // Game UI state
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    private val _wordState = MutableStateFlow<WordResponseState>(WordResponseState.None)
    val wordState: StateFlow<WordResponseState> = _wordState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String


    init {
        resetGame()
    }


   suspend fun askGemini(word: String) {
        try {
            _wordState.value = WordResponseState.IsLoading
            val response = repository.getWordResponse(WordModel(COMMAND + word))
            Log.d("res", response.body().toString())
            Log.d("res", response.toString())
            _wordState.value = response.body()?.data?.let {
                WordResponseState.Success(it) }!!

        } catch (e: Exception) {
            Log.d("res", e.message.toString())

            _wordState.value = WordResponseState.Error("Something went wrong")
        }
    }

    // Update the guessed word
    fun updateGuessedWord(guessedWord: String) {
        userGuess = guessedWord
    }

    private fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUIState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round
            val updatedScore = _uiState.value.score + SCORE_INCREASE
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateGuessedWord("")
    }

    // Skip to next word
    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateGuessedWord("")
    }

    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

     fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }


}
























