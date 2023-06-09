package com.example.tasksandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.model.ValidationModel
import com.example.tasksandroid.service.listener.APIListener
import com.example.tasksandroid.service.repository.PriorityRepository
import com.example.tasksandroid.service.repository.TasksRepository

class TaskListViewModel(application: Application): AndroidViewModel(application) {

    private val tasksRepository = TasksRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var _taskFilter = 0

    private val _taskList = MutableLiveData<List<TaskModel>>()
    val taskList: LiveData<List<TaskModel>> = _taskList

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun list(taskFilter: Int){
        _taskFilter = taskFilter
        val listener = object : APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {
                for(item in result){
                    item.priorityDescription = priorityRepository.getPriorityById(item.priorityId)
                }
                _taskList.value = result
            }

            override fun onFailure(onMessage: String) {
                _status.value = ValidationModel(onMessage)
            }

        }
        when (taskFilter) {
            TaskConstants.FILTER.ALL -> tasksRepository.list(listener)
            TaskConstants.FILTER.NEXT -> tasksRepository.listNext7Days(listener)
            else -> tasksRepository.listOverdue(listener)
        }
    }

    fun delete(id: Int){
        tasksRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(_taskFilter)
            }

            override fun onFailure(onMessage: String) {
                _delete.value = ValidationModel(onMessage)
            }

        })
    }

    fun changeState(id: Int, isComplete: Boolean){

        if (isComplete){
            tasksRepository.complete(id, object : APIListener<Boolean>{
                override fun onSuccess(result: Boolean) {
                    list(_taskFilter)
                }

                override fun onFailure(onMessage: String) {
                    _status.value = ValidationModel(onMessage)
                }

            })
        }else{
            tasksRepository.undo(id, object : APIListener<Boolean>{
                override fun onSuccess(result: Boolean) {
                    list(_taskFilter)
                }

                override fun onFailure(onMessage: String) {
                    _status.value = ValidationModel(onMessage)
                }

            })
        }
    }

}