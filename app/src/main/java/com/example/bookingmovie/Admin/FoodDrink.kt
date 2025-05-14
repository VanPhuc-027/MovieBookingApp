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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookingmovie.ViewModels.MenuViewModel
import com.example.bookingmovie.data.Item.ItemEntity

@Composable
fun FoodDrinkScreen(viewModel: MenuViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ItemEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<ItemEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val foodList by viewModel.foodDrinks.collectAsState()
    val filteredList = foodList
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .sortedBy { it.quantity }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("MENU", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Tìm kiếm món") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Tổng số món: ${filteredList.size}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontWeight = FontWeight.Medium
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
                                Text(text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 8.dp))
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
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = {
                                    itemToDelete = item
                                    showDeleteDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xoá",
                                        tint = MaterialTheme.colorScheme.error
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
                        viewModel.addFoodDrink(item.name, item.price, item.quantity)
                    } else {
                        viewModel.updateFoodDrink(item)
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
                        viewModel.deleteFoodDrink(itemToDelete!!)
                        showDeleteDialog = false
                        itemToDelete = null
                    }) {
                        Text("Xoá", color = MaterialTheme.colorScheme.error)
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
    initialItem: ItemEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (ItemEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var price by remember { mutableStateOf(initialItem?.price?.toString() ?: "") }
    var quantity by remember { mutableStateOf(initialItem?.quantity?.toString() ?: "") }

    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "Thêm món" else "Sửa món") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; showError = false },
                    label = { Text("Tên món") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() } },
                    label = { Text("Giá") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter { char -> char.isDigit() } },
                    label = { Text("Số lượng tồn") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (showError) {
                    Text(
                        text = "Vui lòng nhập đầy đủ và đúng định dạng (giá, tồn kho là số).",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val parsedPrice = price.toIntOrNull()
                    val parsedQuantity = quantity.toIntOrNull()

                    if (name.isNotBlank() && parsedPrice != null && parsedQuantity != null) {
                        onConfirm(
                            ItemEntity(
                                id = initialItem?.id ?: 0,
                                name = name.trim(),
                                price = parsedPrice,
                                quantity = parsedQuantity
                            )
                        )
                    } else {
                        showError = true
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
