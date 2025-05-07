package com.example.bookingmovie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ui.theme.BookingMovieTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LoginForm(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isFormValid = username.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier
            .padding(24.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Placeholder
        Text(
            text = " Welcome To",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003399) // Xanh đậm
        )

        Spacer(modifier = Modifier.height(8.dp))



        Spacer(modifier = Modifier.height(32.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            placeholder = { Text("Enter your username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            },
            enabled = isFormValid,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935), // Đỏ
                disabledContainerColor = Color(0xFFCCCCCC)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(8.dp, shape = RoundedCornerShape(50))
        ) {
            Text(text = "Login", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot password
        TextButton(onClick = { /* TODO: handle forgot password */ }) {
            Text(
                text = "Forgot username / password?",
                fontSize = 14.sp,
                color = Color(0xFF1976D2)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BookingMovieTheme {
        LoginScreen(navController = rememberNavController())
    }
}
