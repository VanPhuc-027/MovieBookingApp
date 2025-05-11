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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCategory() {
    var categories by remember {
        mutableStateOf(
            mutableListOf("Lãng Mạn", "Hoạt Hình", "Chiến Tranh", "Tình Cảm", "Hành Động")
        )
    }
    var newCategory by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Thể loại",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D1B2A)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEditing && editingIndex in categories.indices) {
                        categories = categories.toMutableList().also {
                            it[editingIndex] = newCategory
                        }
                        isEditing = false
                        editingIndex = -1
                    } else if (newCategory.isNotBlank()) {
                        categories = categories.toMutableList().also {
                            it.add(newCategory)
                        }
                    }
                    newCategory = ""
                },
                containerColor = Color(0xFF0D1B2A),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm thể loại")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ô nhập tên thể loại
            TextField(
                value = newCategory,
                onValueChange = { newCategory = it },
                placeholder = { Text("Tên thể loại...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                }
            )

            Divider()

            // Danh sách thể loại
            categories.forEachIndexed { index, category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Sửa",
                            tint = Color(0xFF1E88E5),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    newCategory = category
                                    isEditing = true
                                    editingIndex = index
                                }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xoá",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    categories = categories.toMutableList().also {
                                        it.removeAt(index)
                                    }
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
