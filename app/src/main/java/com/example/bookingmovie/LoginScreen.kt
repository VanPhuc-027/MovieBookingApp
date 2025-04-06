package com.example.bookingmovie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookingmovie.ui.theme.BookingMovieTheme

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "CHÀO MỪNG TRỞ LẠI",
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 100.dp)
            )
            TextField(
                modifier = Modifier.padding(top = 100.dp),
                value = "Tài khoản",
                onValueChange = {}
            )
            TextField(
                modifier = Modifier.padding(top = 10.dp),
                value = "Mật khẩu",
                onValueChange = {}
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start
            )
            {
                RadioButton(
                    onClick = {/*TODO*/},
                    selected = selectedOption == "Option 1"
                )
                Text(
                    text = "User",
                    modifier = Modifier.padding(top = 10.dp)
                )
                RadioButton(
                    selected = selectedOption == "Option 2",
                    onClick = {/*TODO*/}
                )
                Text(
                    text = "Admin",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Button(
                onClick = {/*TODO*/ },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(40.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
                )
            }
            Text(
                text = "Quên mật khẩu",
                modifier = Modifier.padding(top = 15.dp),
                color = Color.Blue
            )
            Row (
                modifier = Modifier.padding(top = 100.dp)
            ){
                Text(
                    text = "Chưa có tài khoản?"
                )
                Text(
                    text = "Đăng ký",
                    color = Color.Yellow
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookingMovieTheme {
        LoginScreen(navController = rememberNavController())
    }
}