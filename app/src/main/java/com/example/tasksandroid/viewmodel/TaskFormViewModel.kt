package com.example.tasksandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasksandroid.model.PriorityModel
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.model.ValidationModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.PriorityRepository
import com.example.tasksandroid.service.repository.TasksRepository

class TaskFormViewModel(application: Application): AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TasksRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> =_taskSave

    fun loadPriority(){
        _priorityList.value = priorityRepository.listPriority().toList()
    }

    fun save(task: TaskModel){
        taskRepository.create(task, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel()
            }

            override fun onFailure(onMessage: String) {
                _taskSave.value = ValidationModel(onMessage)
            }

        })

    }
}