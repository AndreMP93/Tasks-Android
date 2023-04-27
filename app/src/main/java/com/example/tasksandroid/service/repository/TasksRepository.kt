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
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )
        executeCall(call, listener)
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.update(
            task.id,
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )
        executeCall(call, listener)
    }

    fun list(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.listTasks(), listener)
    }

    fun listNext7Days(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.listTasksNext7Days(), listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.listTasksOverdue(), listener)
    }

    fun load(id: Int, listener: APIListener<TaskModel>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.load(id), listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.delete(id), listener)
    }

    fun complete(id: Int, listener: APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.complete(id), listener)
    }

    fun undo(id: Int, listener: APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.undo(id), listener)
    }
}