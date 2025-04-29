package com.example.bookingmovie.Admin

import androidx.compose.foundation.layout.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MovieCategory() {
    var categories by remember { mutableStateOf(
        mutableListOf("Hành động", "Tình cảm", "Hài hước", "Kinh dị")
    ) }
    var newCategory by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Quản lý thể loại",
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D1B2A)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = newCategory,
                onValueChange = { newCategory = it },
                placeholder = { Text("Nhập tên thể loại") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (isEditing && editingIndex in categories.indices) {
                        categories = categories.toMutableList().also {
                            it[editingIndex] = newCategory
                        }
                        isEditing = false
                        editingIndex = -1
                    } else {
                        if (newCategory.isNotBlank()) {
                            categories = categories.toMutableList().also {
                                it.add(newCategory)
                            }
                        }
                    }
                    newCategory = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Lưu chỉnh sửa" else "Thêm thể loại")
            }

            Divider()

            categories.forEachIndexed { index, category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )

                    Row {
                        Text(
                            text = "Sửa",
                            color = Color(0xFF1E88E5),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    newCategory = category
                                    isEditing = true
                                    editingIndex = index
                                }
                        )

                        Text(
                            text = "Xoá",
                            color = Color.Red,
                            modifier = Modifier
                                .clickable {
                                    categories = categories.toMutableList().also {
                                        it.removeAt(index)
                                    }
                                    // Nếu đang sửa mục bị xoá
                                    if (isEditing && editingIndex == index) {
                                        isEditing = false
                                        editingIndex = -1
                                        newCategory = ""
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieCategory() {
    MovieCategory()
}
