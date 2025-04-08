package com.example.uthapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthapp.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val tasksList = mutableListOf<Task>()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchTasks()
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                tasksList.add(task)
                _tasks.value = tasksList.toList()
                Log.d("TaskViewModel", "Đã thêm task mới: ${task.title}")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Lỗi khi thêm task: ${e.message}")
                _error.value = "Không thể thêm task: ${e.message}"
            }
        }
    }

    fun fetchTasks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                Log.d("TaskViewModel", "Đang tải danh sách công việc...")
                
                // Nếu danh sách rỗng, thêm dữ liệu mẫu
                if (tasksList.isEmpty()) {
                    tasksList.addAll(
                        listOf(
                            Task(
                                id = 1,
                                title = "Task 1",
                                description = "Description 1",
                                status = "Pending",
                                priority = "High",
                                dueDate = "2024-03-20"
                            ),
                            Task(
                                id = 2,
                                title = "Task 2",
                                description = "Description 2",
                                status = "Completed",
                                priority = "Medium",
                                dueDate = "2024-03-21"
                            )
                        )
                    )
                }
                
                _tasks.value = tasksList.toList()
                Log.d("TaskViewModel", "Đã tải ${tasksList.size} công việc")
                
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Lỗi khi tải danh sách công việc: ${e.message}")
                _error.value = "Không thể tải danh sách công việc: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 