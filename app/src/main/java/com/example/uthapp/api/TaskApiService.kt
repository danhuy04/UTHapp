package com.example.uthapp.api

import com.example.uthapp.data.Task
import com.example.uthapp.data.TaskResponse
import retrofit2.http.*

interface TaskApiService {
    
    @GET("tasks") //lay danh sach task 
    suspend fun getTasks(): TaskResponse

    @GET("tasks/{id}") //lay mot task theo id
    suspend fun getTaskById(@Path("id") taskId: Int): TaskResponse

    @POST("tasks") //tao task
    suspend fun createTask(@Body task: Task): TaskResponse

    @PUT("tasks/{id}") //cap nhat
    suspend fun updateTask(@Path("id") taskId: Int, @Body task: Task): TaskResponse

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: Int): TaskResponse
} 