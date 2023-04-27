package com.example.tasksandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun logout(){
        securityPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
        securityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
    }

    fun loadUserName(){
        _userName.value = securityPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }
}