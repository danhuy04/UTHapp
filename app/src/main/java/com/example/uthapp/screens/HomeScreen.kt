package com.example.uthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uthapp.R
import com.example.uthapp.data.Task
import com.example.uthapp.viewmodel.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.uthapp.screens.EmptyTaskScreen

// Màn hình chính của ứng dụng
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    taskViewModel: TaskViewModel
) {
    val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()
    val isLoading by taskViewModel.isLoading.collectAsStateWithLifecycle()
    val error by taskViewModel.error.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "UTH Tasks",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { /* TODO */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Calendar") },
                    label = { Text("Calendar") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    label = { Text("") },
                    selected = false,
                    onClick = onAddClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Description, contentDescription = "Files") },
                    label = { Text("Files") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = onSettingsClick
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: "Đã xảy ra lỗi",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { taskViewModel.fetchTasks() }
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                tasks.isEmpty() -> {
                    HomeEmptyTaskScreen()
                }
                else -> {
                    TaskList(
                        tasks = tasks,
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
}

@Composable
fun HomeEmptyTaskScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Chưa có task nào",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Thêm task mới để bắt đầu",
            color = Color.Gray
        )
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                title = task.title,
                description = task.description,
                status = task.status,
                date = task.dueDate,
                backgroundColor = when (task.priority) {
                    "High" -> Color(0xFFFFD6D6) // Đỏ nhạt cho độ ưu tiên cao
                    "Medium" -> Color(0xFFF3FFD6) // Xanh lá nhạt cho độ ưu tiên trung bình
                    else -> Color(0xFFD6F6FF) // Xanh dương nhạt cho độ ưu tiên thấp
                },
                onClick = { onTaskClick(task.id) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Component hiển thị một task
@Composable
fun TaskItem(
    title: String,
    description: String,
    status: String,
    date: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Checkbox(
                    checked = status == "Completed",
                    onCheckedChange = null,
                    enabled = false
                )
            }
            
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Trạng thái: $status",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Hạn: $date",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}