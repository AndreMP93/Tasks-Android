package com.example.tasksandroid.service.repository

import com.example.tasksandroid.service.repository.remote.RetrofitClient
import com.example.tasksandroid.service.repository.remote.TasksService

class TasksRepository {

    private val remote = RetrofitClient.getServices(TasksService::class.java)
}