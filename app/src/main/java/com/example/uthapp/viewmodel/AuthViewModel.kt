package com.example.uthapp.viewmodel

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log

class AuthViewModel : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    private val _userPhotoUrl = MutableStateFlow<String?>(null)
    val userPhotoUrl: StateFlow<String?> = _userPhotoUrl

    private lateinit var googleSignInClient: GoogleSignInClient

    private val _signInIntent = MutableStateFlow<Intent?>(null)
    val signInIntent: StateFlow<Intent?> = _signInIntent

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Kiểm tra người dùng đã đăng nhập chưa
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    _isAuthenticated.value = true
                    updateUserInfo(currentUser)
                    return@launch
                }

                // Cấu hình Google Sign In
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("282871481574-sq6buktd8l44okd6c6sspjs8jnhvl2vo.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

                googleSignInClient = GoogleSignIn.getClient(context, gso)
                _signInIntent.value = googleSignInClient.signInIntent

            } catch (e: Exception) {
                _error.value = "Lỗi đăng nhập: ${e.message}"
                Log.e("AuthViewModel", "Lỗi đăng nhập Google", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult()
                val idToken = account.idToken

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = Firebase.auth.signInWithCredential(credential).await()
                
                result.user?.let { user ->
                    _isAuthenticated.value = true
                    updateUserInfo(user)
                }
            } catch (e: Exception) {
                _error.value = "Lỗi xử lý đăng nhập: ${e.message}"
                Log.e("AuthViewModel", "Lỗi xử lý đăng nhập", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                if (::googleSignInClient.isInitialized) {
                    googleSignInClient.signOut().await()
                }
                Firebase.auth.signOut()
                _isAuthenticated.value = false
                _userEmail.value = null
                _userName.value = null
                _userPhotoUrl.value = null
            } catch (e: Exception) {
                _error.value = "Lỗi đăng xuất: ${e.message}"
            }
        }
    }

    private fun updateUserInfo(user: com.google.firebase.auth.FirebaseUser) {
        _userEmail.value = user.email
        _userName.value = user.displayName
        _userPhotoUrl.value = user.photoUrl?.toString()
    }
} 