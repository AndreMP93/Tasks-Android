package com.example.tasksandroid.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.R
import com.example.tasksandroid.model.PersonModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.remote.PersonService
import com.example.tasksandroid.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository (context: Context): BaseRepository(context){

    private val remoteCall = RetrofitClient.getServices(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<PersonModel>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remoteCall.login(email, password)
        executeCall(call, listener)
    }

    fun create(name: String, email: String, password: String, listener: APIListener<PersonModel>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remoteCall.create(name, email, password)
        executeCall(call, listener)
    }
}