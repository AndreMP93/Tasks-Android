package com.example.tasksandroid.service.repository.remote

import com.example.tasksandroid.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    fun list(): Call<List<PriorityModel>>
}