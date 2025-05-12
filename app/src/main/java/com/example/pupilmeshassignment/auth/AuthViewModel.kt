@file:Suppress("UNCHECKED_CAST")

package com.example.pupilmeshassignment.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pupilmeshassignment.BuildConfig
import com.example.pupilmeshassignment.data.entity.User
import com.example.pupilmeshassignment.data.repository.UserRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private lateinit var credentialManager: CredentialManager

    private val _signInResult = MutableStateFlow<SignInResult>(SignInResult.Initial)
    val signInResult: StateFlow<SignInResult> = _signInResult


    //Sign in With Email and password
    fun signIn(email: String, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInResult.value = SignInResult.Loading
            try {
                val existingUser = userRepository.getUserByEmail(email)

                if (existingUser != null) {
                    val isValid = userRepository.isValidUser(email, password)
                    if (isValid) {
                        _signInResult.value = SignInResult.Success
                    } else {
                        _signInResult.value = SignInResult.Error("Invalid Password")
                    }
                } else {
                    val newUser = User(email, password)
                    userRepository.insertUser(newUser)
                    _signInResult.value = SignInResult.Success
                }
            } catch (e: Exception) {
                _signInResult.value = SignInResult.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    //Sign in Google Credential Manager
    fun initialize(context: Context) {
        credentialManager = CredentialManager.create(context)
    }

    fun onGoogleSignInClicked(context: Context) {
        val nonce = generateNonce()
        val googleIdOption = createGoogleIdOption(nonce)
        val request = createCredentialRequest(googleIdOption)

        launchSignInRequest(request, context)
    }

    private fun generateNonce(): String {
        return java.util.UUID.randomUUID().toString()
    }

    private fun createGoogleIdOption(nonce: String): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setNonce(nonce)
            .build()
    }

    private fun createCredentialRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    private fun launchSignInRequest(request: GetCredentialRequest, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(context, request)
                handleGoogleSignIn(result)
            } catch (e: NoCredentialException) {
                Log.e("CHECK-->", "No Credential Found: ${e.message}")
            } catch (e: GetCredentialException) {
                Log.e("CHECK-->", "Get Credential ${e.message}")
            }
        }
    }

    private fun handleGoogleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential) {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    signIn(googleIdCredential.id, null)
                    Log.e("CHECK-->", "Email: ${googleIdCredential.id}")
                } catch (e: Exception) {
                    Log.e("CHECK-->", "${e.message}")
                }
            }
        }
    }


    fun resetSignInResult() {
        _signInResult.value = SignInResult.Initial
    }
}

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}

sealed class SignInResult {
    data object Loading : SignInResult()
    data object Success : SignInResult()
    data class Error(val message: String) : SignInResult()
    data object Initial : SignInResult()
}