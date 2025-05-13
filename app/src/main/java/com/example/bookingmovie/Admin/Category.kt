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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.bookingmovie.ViewModels.GenreViewModel
import com.example.bookingmovie.ViewModels.MovieViewModel
import com.example.bookingmovie.data.Genre.GenreEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCategory(viewModel: GenreViewModel=androidx.lifecycle.viewmodel.compose.viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val genres by viewModel.genres.collectAsState()
    var newDescription by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf(-1) }
    var categories by remember {
        mutableStateOf(
            mutableListOf("Lãng Mạn", "Hoạt Hình", "Chiến Tranh", "Tình Cảm", "Hành Động")
        )
    }
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
                onClick = {showDialog = true},
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
            if (genres.isEmpty()) {
                Text("Chưa có thể loại nào", color = Color.Gray)
            }

            genres.forEach { genre ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(text = genre.genre_name, fontSize = 16.sp)
                        Text(text = genre.genre_descripsion, fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Sửa",
                        tint = Color.Blue,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                newCategory = genre.genre_name
                                newDescription = genre.genre_descripsion
                                editingIndex = genre.genre_id
                                isEditing = true
                                showDialog = true
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xoá",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                viewModel.deleteGenre(genre)
                            }
                    )
                }
            }
        }
        if (showDialog) {
            AddGenreDialog(
                onDismiss = {
                    showDialog = false
                    isEditing = false
                },
                initialName = newCategory,
                initialDescription = newDescription,
                onSave = { name, desc ->
                    if (isEditing) {
                        val updatedGenre = GenreEntity(
                            genre_id = editingIndex,
                            genre_name = name,
                            genre_descripsion = desc
                        )
                        viewModel.updateGenre(updatedGenre)
                    } else {
                        viewModel.addGenre(name, desc)
                    }
                    showDialog = false
                    isEditing = false
                }
            )
        }
    }
}


@Composable
fun AddGenreDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    initialName: String = "",
    initialDescription: String = ""
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name.trim(), description.trim())
                    }
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        },
        title = { Text("Thể loại") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên thể loại") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewMovieCategory() {
    MovieCategory()
}
