package com.example.uthapp.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uthapp.R
import com.example.uthapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Theo dõi trạng thái đăng nhập
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
    val userEmail by authViewModel.userEmail.collectAsStateWithLifecycle()
    val userName by authViewModel.userName.collectAsStateWithLifecycle()
    val userPhotoUrl by authViewModel.userPhotoUrl.collectAsStateWithLifecycle()

    // Launcher cho Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            authViewModel.handleSignInResult(result.data)
        }
    }

    // Theo dõi SignInIntent
    val signInIntent by authViewModel.signInIntent.collectAsStateWithLifecycle()
    LaunchedEffect(signInIntent) {
        signInIntent?.let { intent ->
            launcher.launch(intent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isAuthenticated) {
                // Hiển thị thông tin người dùng đã đăng nhập
                Text(
                    text = "Thông tin tài khoản",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Tên: ${userName ?: "Chưa có tên"}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Email: ${userEmail ?: "Chưa có email"}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { authViewModel.signOut() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Đăng xuất")
                }
            } else {
                // Hiển thị nút đăng nhập
                Text(
                    text = "Đăng nhập để đồng bộ dữ liệu",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        isLoading = true
                        authViewModel.signInWithGoogle(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Đăng nhập với Google")
                    }
                }
            }

            // Hiển thị loading
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            // Hiển thị lỗi
            error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
} 