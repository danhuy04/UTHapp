package com.example.uthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthapp.R
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(onNavigateToFirst: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(800)
        onNavigateToFirst()
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Logo",
                modifier = Modifier
                    .width(102.dp)
                    .height(70.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "UTH SmartTask",
                color = Color(0xFF2196F3),

                fontSize = 28.sp,
                modifier = Modifier.width(233.dp)
            )
        }
    }
} 