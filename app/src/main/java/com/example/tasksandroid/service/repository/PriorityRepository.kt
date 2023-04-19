package com.example.tasksandroid.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.R
import com.example.tasksandroid.model.PriorityModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.local.TaskDatabase
import com.example.tasksandroid.service.repository.remote.PriorityService
import com.example.tasksandroid.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) {

    private val remoteCall = RetrofitClient.getServices(PriorityService::class.java)
    private val dataBase = TaskDatabase.getDatabase(context).priorityDAO()

    fun listPriority(listener: APIListener<List<PriorityModel>>){
        val call = remoteCall.list()
        call.enqueue(object : Callback<List<PriorityModel>>{
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>){
                if(response.code() == TaskConstants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{

                    listener.onFailure(failureResponse(response.errorBody().toString()))
                }
            }
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))

            }
        })
    }

    fun savePriorities(list: List<PriorityModel>){
        dataBase.clear()
        dataBase.save(list)
    }

    private fun failureResponse(str: String): String{
        return Gson().fromJson(str, String::class.java)
    }

}