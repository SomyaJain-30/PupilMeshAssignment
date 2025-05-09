@file:Suppress("UNCHECKED_CAST")

package com.example.pupilmeshassignment.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pupilmeshassignment.data.entity.User
import com.example.pupilmeshassignment.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signInResult = MutableStateFlow<SignInResult>(SignInResult.Initial)
    val signInResult: StateFlow<SignInResult> = _signInResult

    fun signIn(email: String, password: String) {
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