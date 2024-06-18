package com.example.scrramble.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomCircularProgressIndicator(label:String, animatedProgress: Float) {


    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .background(Color.Gray.copy(alpha = 0.1f), CircleShape)){
        CircularProgressIndicator(progress = {animatedProgress},
            strokeWidth = 5.dp,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp),
        )
        Text(
            label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)

    }
}

