package com.example.bookingmovie.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FoodItem(
    val name: String,
    val price: Int,
    val quantity: Int
)

@Composable
fun FoodDrinkScreen() {
    var searchQuery by remember { mutableStateOf("") }

    var foodList by remember {
        mutableStateOf(
            mutableListOf(
                FoodItem("Bắp rang bơ", 30000, 50),
                FoodItem("Pepsi", 20000, 100),
                FoodItem("Khoai tây chiên", 25000, 30),
                FoodItem("Nước cam", 22000, 60)
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<FoodItem?>(null) }

    var itemToDelete by remember { mutableStateOf<FoodItem?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val filteredList = foodList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MENU",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Tìm kiếm món") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                LazyColumn {
                    items(filteredList) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold)
                                Text("Giá: ${item.price} đ")
                                Text("Tồn kho: ${item.quantity}")
                            }

                            Row {
                                IconButton(onClick = {
                                    editingItem = item
                                    showDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Sửa",
                                        tint = Color.Blue
                                    )
                                }
                                IconButton(onClick = {
                                    itemToDelete = item
                                    showDeleteDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xoá",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                        Divider()
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                editingItem = null
                showDialog = true
            },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm món", tint = Color.White)
        }

        if (showDialog) {
            FoodItemDialog(
                initialItem = editingItem,
                onDismiss = {
                    showDialog = false
                    editingItem = null
                },
                onConfirm = { item ->
                    if (editingItem == null) {
                        foodList = (foodList + item).toMutableList()
                    } else {
                        foodList = foodList.map {
                            if (it == editingItem) item else it
                        }.toMutableList()
                    }
                    showDialog = false
                    editingItem = null
                }
            )
        }

        if (showDeleteDialog && itemToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    itemToDelete = null
                },
                title = { Text("Xoá món") },
                text = { Text("Bạn chắc chắn muốn xoá \"${itemToDelete?.name}\" không?") },
                confirmButton = {
                    TextButton(onClick = {
                        foodList.remove(itemToDelete)
                        showDeleteDialog = false
                        itemToDelete = null
                    }) {
                        Text("Xoá", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        itemToDelete = null
                    }) {
                        Text("Huỷ")
                    }
                }
            )
        }
    }
}

@Composable
fun FoodItemDialog(
    initialItem: FoodItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (FoodItem) -> Unit
) {
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var price by remember { mutableStateOf(initialItem?.price?.toString() ?: "") }
    var quantity by remember { mutableStateOf(initialItem?.quantity?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "Thêm món" else "Sửa món") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên món") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Giá") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Số lượng tồn") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && price.toIntOrNull() != null && quantity.toIntOrNull() != null) {
                        onConfirm(FoodItem(name.trim(), price.toInt(), quantity.toInt()))
                    }
                }
            ) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodDrinkScreen() {
    FoodDrinkScreen()
}
