package com.example.tasksandroid.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksandroid.R
import com.example.tasksandroid.databinding.RowTaskListBinding
import com.example.tasksandroid.model.TaskModel
import com.example.tasksandroid.service.listener.TaskListener
import java.text.SimpleDateFormat

class TaskAdapter(private var listTasks: List<TaskModel>, private var listener: TaskListener)
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(task: TaskModel) {

            itemBinding.textDescription.text = task.description
            itemBinding.textPriority.text = task.priorityDescription

            val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate)
            itemBinding.textDueDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)

            // Faz o tratamento para tarefas já completas
            if (task.complete) {
                itemBinding.imageTask.setImageResource(R.drawable.ic_done)
            } else {
                itemBinding.imageTask.setImageResource(R.drawable.ic_todo)
            }

            // Eventos
            itemBinding.textDescription.setOnClickListener { listener.onListClick(task.id) }
            itemBinding.imageTask.setOnClickListener {
                if (task.complete) {
                    listener.onUndoClick(task.id)
                } else {
                    listener.onCompleteClick(task.id)
                }
            }

            itemBinding.textDescription.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.remocao_de_tarefa)
                    .setMessage(R.string.remover_tarefa)
                    .setPositiveButton(R.string.sim) { dialog, which ->
                        listener.onDeleteClick(task.id)
                    }
                    .setNeutralButton(R.string.cancelar, null)
                    .show()
                true
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowTaskListBinding.inflate(inflater, parent, false)
        return TaskViewHolder(itemBinding, listener)
    }

    override fun getItemCount(): Int {
        return listTasks.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    fun updateTasks(list: List<TaskModel>) {
        listTasks = list
        notifyDataSetChanged()
    }
}