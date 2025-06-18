package com.example.managertask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.managertask.data.Task
import com.example.managertask.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskDeleted: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit,
    private val onTaskStarred: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        private val timeFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())

        fun bind(task: Task) {
            binding.apply {
                taskTitle.text = task.title
                taskDescription.text = task.description
                taskDueDate.text = "Due: ${dateFormat.format(task.dueDate)}"
                taskCreatedAt.text = "Created: ${timeFormat.format(task.createdAt)}"
                
                // Handle checkbox independently
                taskCheckBox.isChecked = task.isCompleted
                taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(task, isChecked)
                }

                // Handle delete button
                deleteButton.setOnClickListener {
                    onTaskDeleted(task)
                }

                // Handle edit button
                editButton.setOnClickListener {
                    onTaskEdit(task)
                }

                // Handle star button independently
                if (task.isFavourite) {
                    starButton.setImageResource(android.R.drawable.btn_star_big_on)
                    starButton.imageTintList = android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor("#FFD700") // Gold color for starred
                    )
                } else {
                    starButton.setImageResource(android.R.drawable.btn_star_big_off)
                    starButton.imageTintList = android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor("#757575") // Gray color for unstarred
                    )
                }
                starButton.setOnClickListener {
                    onTaskStarred(task)
                }
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
} 