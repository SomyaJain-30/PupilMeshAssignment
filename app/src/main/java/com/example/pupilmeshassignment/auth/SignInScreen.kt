package com.example.pupilmeshassignment.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pupilmeshassignment.R
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pupilmeshassignment.MyApp
import com.example.pupilmeshassignment.data.AppContainer
import com.example.pupilmeshassignment.data.MyDatabase
import com.example.pupilmeshassignment.data.repository.UserRepository
import com.example.pupilmeshassignment.utils.DataStoreManager
import kotlin.math.sign

@Composable
fun SignInScreen(viewModel: AuthViewModel = getAuthViewModel(), onSignInSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isPasswordVisible by remember { mutableStateOf(false) }
    var isEmailValidated by remember { mutableStateOf(false) }
    var isPasswordValidated by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val signInResultState by viewModel.signInResult.collectAsState()
    val dataStoreManager = remember { DataStoreManager.getInstance(context) }

    LaunchedEffect(System.currentTimeMillis(), signInResultState) {
        when (signInResultState) {
            is SignInResult.Loading -> {
                isLoading = true
                Log.e("CHECK-->", "SignInScreen: Loading")
            }

            is SignInResult.Success -> {
                isLoading = false
                onSignInSuccess()
                dataStoreManager.setUserSignedIn(true)
                Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                viewModel.resetSignInResult()

            }

            is SignInResult.Error -> {
                isLoading = false
                Toast.makeText(
                    context,
                    (signInResultState as SignInResult.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("CHECK-->", "SignInScreen: Error")
                viewModel.resetSignInResult()
            }

            is SignInResult.Initial -> {
                isLoading = false
                Log.e("CHECK-->", "SignInScreen: Initial")
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}, modifier = Modifier.padding(end = 4.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
            Text(
                text = "Sign In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Zenithra",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Welcome back",
                        fontSize = 26.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please enter your details to sign in",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_login),
                                "Google Login",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_apple_login),
                                "Apple Login",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "OR",
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.weight(1f),
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailValidated =
                                android.util.Patterns.EMAIL_ADDRESS.matcher(it)
                                    .matches()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Your Email Address",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        },
                        isError = email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                            email
                        ).matches(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.White
                        ),
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            isPasswordValidated = it.isNotBlank()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Password",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {

                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(onClick = {}) {
                            Text(
                                text = "Forgot Password?",
                                style = TextStyle(textDecoration = TextDecoration.Underline),
                                color = Color.Cyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { viewModel.signIn(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isPasswordValidated && isEmailValidated && !isLoading,
                        colors = ButtonDefaults.buttonColors(contentColor = if (isPasswordValidated && isEmailValidated) Color.White else Color.Gray)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = "Sign In")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Don't have an account?",
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                        TextButton(
                            onClick = {},
                            modifier = Modifier.padding(0.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Sign Up",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun SignInScreenPreview() {
    SignInScreen(onSignInSuccess = {})
}

@Composable
fun getAuthViewModel(): AuthViewModel {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as MyApp).container
    val repository = appContainer.userRepository
    return viewModel(factory = AuthViewModelFactory(repository))
}