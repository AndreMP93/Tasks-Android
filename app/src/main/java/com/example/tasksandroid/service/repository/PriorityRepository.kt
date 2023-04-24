package com.example.tasksandroid.service.repository

import android.content.Context
import com.example.tasksandroid.R
import com.example.tasksandroid.model.PriorityModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.local.TaskDatabase
import com.example.tasksandroid.service.repository.remote.PriorityService
import com.example.tasksandroid.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context): BaseRepository(context) {

    private val remoteCall = RetrofitClient.getServices(PriorityService::class.java)
    private val dataBase = TaskDatabase.getDatabase(context).priorityDAO()

    companion object{
        private val cache = mutableMapOf<Int, String>()
        private fun getDescription(id: Int): String{
            return cache[id] ?: ""
        }

        private fun setDescription(id: Int, description: String){
            cache[id] = description
        }
    }

    fun listPriority(listener: APIListener<List<PriorityModel>>){
        executeCall(remoteCall.list(), listener)
    }

    fun savePriorities(list: List<PriorityModel>){
        dataBase.clear()
        dataBase.save(list)
    }

    fun listPriority(): List<PriorityModel>{
        return dataBase.list()

    }

    fun getPriorityById(id: Int): String{
        var description = getDescription(id)
        if (description == ""){
            description = dataBase.getPriorityById(id)
            setDescription(id, description)
            return description
        }
        return description
    }


}