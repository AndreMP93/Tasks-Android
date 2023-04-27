package com.example.tasksandroid.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.R
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {

    fun failureResponse(str: String): String{
        return Gson().fromJson(str, String::class.java)
    }

    fun <T> executeCall(call: Call<T>, listener: APIListener<T>){
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{
                    listener.onFailure(failureResponse(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun isConnectionAvailable(): Boolean{
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork  = connectivityManager.activeNetwork?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        result = when{
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return result
    }
}