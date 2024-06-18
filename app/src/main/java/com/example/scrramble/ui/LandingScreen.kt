package com.example.scrramble.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.example.scrramble.R

class LandingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Image(painterResource(id = R.drawable.logo), "")

                Button(onClick = {
                    navigator?.push(HomeScreen())
                }) {
                    Text(
                        text = "PLAY", fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }

            }
        }

    }
}