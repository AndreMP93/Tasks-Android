package com.example.tasksandroid.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.R
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.remote.RetrofitClient
import com.example.tasksandroid.service.repository.remote.TasksService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksRepository(context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.getServices(TasksService::class.java)

    fun create(task: TaskModel, listener: APIListener<Boolean>){
        val call = remote.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )
        executeCall(call, listener)
    }

    fun list(listener: APIListener<List<TaskModel>>){
        executeCall(remote.listTasks(), listener)
    }

    fun listNext7Days(listener: APIListener<List<TaskModel>>){
        executeCall(remote.listTasksNext7Days(), listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){
        executeCall(remote.listTasksOverdue(), listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>){
        executeCall(remote.delete(id), listener)
    }

}