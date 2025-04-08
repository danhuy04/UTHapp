package com.example.uthapp.data

// Trạng thái phản hồi từ API
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val message: String,
    val data: T
)

// Model task chính
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val priority: String,
    val dueDate: String
)

// Công việc con
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

// Tệp đính kèm
data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

// Nhắc nhở
data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

// trang thai phan hoi tu api
data class TaskResponse(
    val isSuccess: Boolean = true, 
    val message: String = "",
    val data: List<Task> = emptyList()
)