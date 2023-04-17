package com.example.tasksandroid.service.listener

interface APIListener<T> {
    fun onSuccess(result: T)
    fun onFailure(onMessage: String)
}