package com.example.tasksandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.model.PersonModel
import com.example.tasksandroid.model.ValidationModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.PersonRepository
import com.example.tasksandroid.service.repository.SecurityPreferences
import com.example.tasksandroid.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application): AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    fun register(name: String, email: String, password: String){
        personRepository.create(name, email, password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)
                println("TESTE 1: ViewMode")
                RetrofitClient.addHearder(result.token, result.personKey)
                println("TESTE 2: ViewMode")

                _user.value = ValidationModel()

            }

            override fun onFailure(onMessage: String) {
                _user.value = ValidationModel(onMessage)
            }

        })
    }
}