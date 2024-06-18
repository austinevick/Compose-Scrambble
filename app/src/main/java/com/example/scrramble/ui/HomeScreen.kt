package com.example.scrramble.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.scrramble.R
import com.example.scrramble.composable.CustomCircularProgressIndicator
import com.example.scrramble.data.MAX_NO_OF_WORDS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<MainViewModel>()
        val uiState = viewModel.uiState.collectAsState()
        val wordResponseState = viewModel.wordState.collectAsState()
        val scope = rememberCoroutineScope()
        val timeLeft = remember { mutableIntStateOf(30) }
        val calculateTimeProgress = animateFloatAsState(
            targetValue = timeLeft.intValue.toFloat() / 30f, label = ""
        )
        val animatedProgress = animateFloatAsState(
            targetValue = calculateTimeProgress.value * 100, label = ""
        )
        val calculatePercentage = animatedProgress.value.roundToInt()

        suspend fun startTimer() {
            while (timeLeft.intValue > 0) {
                delay(1000L)
                timeLeft.intValue--
            }
            if (timeLeft.intValue == 0) {
                timeLeft.intValue = 30
                viewModel.skipWord()
            }
        }

        LaunchedEffect(timeLeft.intValue) {
            startTimer()
        }



        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(text = "Scrambble".uppercase(Locale.ROOT))
                })
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.word_count,
                                uiState.value.currentWordCount
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    CustomCircularProgressIndicator(
                        label = "${timeLeft.intValue}s",
                        animatedProgress = calculateTimeProgress.value,
                    )
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.score, uiState.value.score),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    uiState.value.currentScrambledWord
                        .toCharArray().map {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(38.dp)
                                    .background(
                                        Color.Gray.copy(alpha = 0.1f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = it.toString(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.instruction))

                OutlinedTextField(
                    value = viewModel.userGuess,
                    onValueChange = { viewModel.updateGuessedWord(it) },
                    label = {
                        if (uiState.value.isGuessedWordWrong) {
                            Text(text = stringResource(id = R.string.wrong_guess))
                        } else {
                            Text(text = stringResource(id = R.string.enter_your_word))
                        }
                    }, singleLine = true,
                    textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
                    isError = uiState.value.isGuessedWordWrong
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.checkUserGuess() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Submit")
                }
                val width = LocalConfiguration.current.screenWidthDp.dp
                    OutlinedButton(
                        onClick = {
                            viewModel.skipWord()
                            timeLeft.intValue = 30
                        },
                        modifier = Modifier.width(width),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Skip")
                    }

                when (wordResponseState.value) {
                    is WordResponseState.IsLoading -> {
                        CircularProgressIndicator()
                    }

                    is WordResponseState.Error -> {
                        Text(text = (wordResponseState.value as WordResponseState.Error).message)
                    }

                    is WordResponseState.Success -> {
                        val data = wordResponseState.value as WordResponseState.Success
                        Text(text = data.data)
                    }
                    else ->{}
                }

            }
        }
    }
}










