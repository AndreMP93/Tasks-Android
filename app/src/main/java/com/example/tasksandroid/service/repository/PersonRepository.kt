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

class PersonRepository (val context: Context){

    private val remoteCall = RetrofitClient.getServices(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<PersonModel>){
        val call = remoteCall.login(email, password)
        call.enqueue(object :Callback<PersonModel>{
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{

                    listener.onFailure(failureResponse(response.errorBody().toString()))
                }


            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))

            }

        })
    }

    private fun failureResponse(str: String): String{
        return Gson().fromJson(str, String::class.java)
    }
}