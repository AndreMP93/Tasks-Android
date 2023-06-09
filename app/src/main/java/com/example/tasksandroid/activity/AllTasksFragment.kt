package com.example.tasksandroid.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.example.tasksandroid.activity.adapter.TaskAdapter
import com.example.tasksandroid.databinding.FragmentAllTasksBinding
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.service.listener.TaskListener
import com.example.tasksandroid.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskListViewModel
    private var taskList: List<TaskModel> = arrayListOf()
    private lateinit var taskAdapter: TaskAdapter
    private var taskFilter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        observes()
        viewModel.list(taskFilter)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(taskList, object : TaskListener{
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                viewModel.changeState(id, true)

            }

            override fun onUndoClick(id: Int) {
                viewModel.changeState(id, false)
            }

        })
        binding.recyclerView.adapter = taskAdapter



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list(taskFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observes(){
        viewModel.taskList.observe(viewLifecycleOwner){
            taskList = it
            taskAdapter.updateTasks(taskList)
        }

        viewModel.delete.observe(viewLifecycleOwner){
            if (!it.status()){
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.status.observe(viewLifecycleOwner){
            if (!it.status()){
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}