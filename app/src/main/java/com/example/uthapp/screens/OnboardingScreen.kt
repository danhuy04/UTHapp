package com.example.uthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uthapp.R

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(1) }

    when (currentScreen) {
        1 -> FirstScreen(
            onNavigateToSecond = { currentScreen = 2 }
        )
        2 -> SecondScreen(
            onNavigateToThird = { currentScreen = 3 },
            onNavigateBack = { currentScreen = 1 }
        )
        3 -> ThirdScreen(
            onNavigateToHome = onNavigateToHome,
            onNavigateBack = { currentScreen = 2 }
        )
    }
}

@Composable
fun FirstScreen(
    onNavigateToSecond: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.first),
                contentDescription = "First Screen",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNavigateToSecond,
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp)
                    .padding(end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Tiếp tục",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SecondScreen(
    onNavigateToThird: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.second),
                contentDescription = "Second Screen",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            Button(
                onClick = onNavigateToThird,
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp)
                    .padding(end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Tiếp tục",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ThirdScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.third),
                contentDescription = "Third Screen",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp)
                    .padding(end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Bắt đầu",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 