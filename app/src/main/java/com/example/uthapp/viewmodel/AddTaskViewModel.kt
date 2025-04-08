package com.example.uthapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthapp.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddTaskViewModel(
    private val taskViewModel: TaskViewModel
) : ViewModel() {
    var title by mutableStateOf("")
        private set
        
    var description by mutableStateOf("")
        private set
        
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    fun onTitleChange(newTitle: String) {
        title = newTitle
    }
    
    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }
    
    fun addTask() {
        if (title.isBlank()) {
            _error.value = "Vui lòng nhập tiêu đề task"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDate.format(formatter)
                
                val newTask = Task(
                    id = System.currentTimeMillis().toInt(),
                    title = title,
                    description = description,
                    status = "Pending",
                    priority = "Medium",
                    dueDate = formattedDate
                )
                
                // Thêm task mới vào TaskViewModel
                taskViewModel.addTask(newTask)
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Không thể thêm task: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetState() {
        title = ""
        description = ""
        _error.value = null
        _isSuccess.value = false
        _isLoading.value = false
    }
} 