package com.example.bookingmovie.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookingmovie.ViewModels.RegisterViewModel
import com.example.bookingmovie.ui.theme.BookingMovieTheme

@Composable
fun RegisterScreenContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    gmail: String,
    onGmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick:() ->Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
        ,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Đăng ký tài khoản",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Họ tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gmail,
            onValueChange = onGmailChange,
            label = { Text("Gmail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Mật khẩu") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = onPasswordVisibilityChange) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Đăng ký")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),

        ) {
            Text("Quay lai")
        }
    }
}

@Composable
fun RegisterScreen(navController : NavController) {
    var username by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            RegisterScreenContent(
                username = username,
                onUsernameChange = { username = it },
                gmail = gmail,
                onGmailChange = { gmail = it },
                phone = phone,
                onPhoneChange = { phone = it },
                password = password,
                onPasswordChange = { password = it },
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                onRegisterClick = {
                    val phoneInt = phone.toIntOrNull()
                    if (username.isNotBlank() && gmail.isNotBlank() && phoneInt != null && password.isNotBlank()) {
                        viewModel.registerUser(username, gmail, phoneInt, password)
                        Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    BookingMovieTheme {
        RegisterScreenContent(
            username = "",
            onUsernameChange = {},
            gmail = "",
            onGmailChange = {},
            phone = "",
            onPhoneChange = {},
            password = "",
            onPasswordChange = {},
            isPasswordVisible = false,
            onPasswordVisibilityChange = {},
            onRegisterClick = {},
            onBackClick = {}
        )
    }
}
