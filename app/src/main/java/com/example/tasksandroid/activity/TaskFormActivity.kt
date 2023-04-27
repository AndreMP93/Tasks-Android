package com.example.tasksandroid.activity

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.R
import com.example.tasksandroid.databinding.ActivityTaskFormBinding
import com.example.tasksandroid.model.PriorityModel
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat

class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var builder: ActivityTaskFormBinding
    private lateinit var viewModel: TaskFormViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var priorityList: List<PriorityModel> = mutableListOf()
    private var taskIdIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        builder = ActivityTaskFormBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        setContentView(builder.root)


        builder.buttonDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, this, year, month, day).show()
        }

        builder.buttonSave.setOnClickListener {
            val task = TaskModel().apply {
                this.id = taskIdIdentification
                this.description = builder.editDescription.text.toString()
                this.complete = builder.checkComplete.isChecked
                this.dueDate = builder.buttonDate.text.toString()
                val index = builder.spinnerPriority.selectedItemPosition
                this.priorityId = priorityList[index].id
                this.priorityDescription = priorityList[index].description
            }

            viewModel.save(task)
        }

        loadDataFromActivity()

        viewModel.loadPriority()
        observe()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val douDate = dateFormat.format(calendar.time)
        builder.buttonDate.text = douDate
    }

    private fun observe(){
        viewModel.priorityList.observe(this){
            priorityList = it
            val listItem = mutableListOf<String>()
            for(p in it){
                listItem.add(p.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listItem)
            builder.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this){
            if(it.status()){
                if(taskIdIdentification == 0){
                    showToast(getString(R.string.task_created))
                }else{
                    showToast(getString(R.string.task_updated))
                }

                finish()
            }else{
                showToast(it.message)
            }
        }

        viewModel.task.observe(this){
            builder.editDescription.setText(it.description)
            builder.spinnerPriority.setSelection(getIndex(it.priorityId))
            builder.checkComplete.isChecked = it.complete
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            builder.buttonDate.text = SimpleDateFormat("yyyy-MM-dd").format(date)
        }

        viewModel.taskLoad.observe(this){
            showToast(it.message)
            finish()
        }
    }

    private fun getIndex(priorityId: Int): Int{
        var index = 0
        for(item in priorityList){
            if(item.id == priorityId){
                break
            }
            index++
        }
        return index
    }

    private fun showToast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if(bundle!= null){
            taskIdIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdIdentification)
        }


    }

}