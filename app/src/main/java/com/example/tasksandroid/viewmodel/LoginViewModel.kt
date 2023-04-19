package com.example.tasksandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.model.PersonModel
import com.example.tasksandroid.model.PriorityModel
import com.example.tasksandroid.model.ValidationModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.PersonRepository
import com.example.tasksandroid.service.repository.PriorityRepository
import com.example.tasksandroid.service.repository.SecurityPreferences
import com.example.tasksandroid.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences =SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> =_login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> =_loggedUser


    fun doLogin(email: String, password: String){
        personRepository.login(email, password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHearder(result.token, result.personKey)

                _login.value = ValidationModel()

            }

            override fun onFailure(onMessage: String) {
                _login.value = ValidationModel(onMessage)
            }

        })

    }

    fun verifyLoggedUser(){
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHearder(token, personKey)
        val logged = (token != "" && personKey != "")
        _loggedUser.value = logged
        if(logged){
            priorityRepository.listPriority(object : APIListener<List<PriorityModel>>{
                override fun onSuccess(result: List<PriorityModel>) {
                    priorityRepository.savePriorities(result)
                }

                override fun onFailure(onMessage: String) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}