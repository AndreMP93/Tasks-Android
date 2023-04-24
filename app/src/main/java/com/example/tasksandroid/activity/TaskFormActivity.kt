package com.example.tasksandroid.activity

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
                this.id = 0
                this.description = builder.editDescription.text.toString()
                this.complete = builder.checkComplete.isChecked
                this.dueDate = builder.buttonDate.text.toString()
                val index = builder.spinnerPriority.selectedItemPosition
                this.priorityId = priorityList[index].id
                this.priorityDescription = priorityList[index].description
            }

            viewModel.save(task)
        }

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
                showToast(getString(R.string.task_created))
                finish()
            }else{
                showToast(it.message)
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}