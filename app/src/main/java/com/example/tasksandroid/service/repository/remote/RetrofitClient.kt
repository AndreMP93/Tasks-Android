package com.example.tasksandroid.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object{
        private lateinit var INSTANCE: Retrofit
        private var token = ""
        private var personKey = ""

        private fun getRetrofitInstance(): Retrofit{
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(TaskConstants.HEADER.TOKEN_KEY,"")
                    .addHeader(TaskConstants.HEADER.PERSON_KEY, "")
                    .build()
                chain.proceed(request)
            })

            if(!::INSTANCE.isInitialized){
                synchronized(RetrofitClient::class.java){
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T> getServices(serviceClass: Class<T>): T{
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHearder(tokenValue: String, personKeyValue: String){
            token = tokenValue
            personKey = personKeyValue
        }
    }
}