package com.example.managertask

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managertask.data.Task
import com.example.managertask.data.TaskDatabase
import com.example.managertask.databinding.ActivityMainBinding
import com.example.managertask.databinding.DialogAddTaskBinding
import com.example.managertask.ui.TaskAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskDao: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        taskDao = TaskDatabase.getDatabase(this)
        setupRecyclerView()
        setupFab()
        observeTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                AuthHelper.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskChecked = { task, isChecked ->
                lifecycleScope.launch {
                    taskDao.taskDao().updateTask(task.copy(isCompleted = isChecked))
                }
            },
            onTaskDeleted = { task ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete") { _, _ ->
                        lifecycleScope.launch {
                            taskDao.taskDao().deleteTask(task)
                            Toast.makeText(this@MainActivity, "Task deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },
            onTaskEdit = { task ->
                showEditTaskDialog(task)
            },
            onTaskStarred = { task ->
                lifecycleScope.launch {
                    taskDao.taskDao().updateTask(task.copy(isFavourite = !task.isFavourite))
                }
            }
        )
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun setupFab() {
        binding.addTaskFab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun observeTasks() {
        try {
            taskDao.taskDao().getAllTasks().observe(this) { tasks ->
                taskAdapter.submitList(tasks)
                binding.emptyStateText.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error observing tasks: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this))
        var selectedDueDate: Date? = null
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        
        // Set up date picker
        dialogBinding.selectDueDateButton.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            
            android.app.DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    selectedDueDate = calendar.time
                    dialogBinding.selectedDueDateText.text = "Due Date: ${dateFormat.format(selectedDueDate!!)}"
                },
                year, month, day
            ).show()
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Add New Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogBinding.taskTitleInput.text.toString()
                val description = dialogBinding.taskDescriptionInput.text.toString()
                if (title.isNotBlank()) {
                    val task = Task(
                        title = title,
                        description = description,
                        dueDate = selectedDueDate ?: Date()
                    )
                    lifecycleScope.launch {
                        taskDao.taskDao().insertTask(task)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditTaskDialog(task: Task) {
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this))
        var selectedDueDate: Date = task.dueDate
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        
        dialogBinding.taskTitleInput.setText(task.title)
        dialogBinding.taskDescriptionInput.setText(task.description)
        dialogBinding.selectedDueDateText.text = "Due Date: ${dateFormat.format(task.dueDate)}"
        
        // Set up date picker
        dialogBinding.selectDueDateButton.setOnClickListener {
            val calendar = java.util.Calendar.getInstance().apply { time = task.dueDate }
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            
            android.app.DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    selectedDueDate = calendar.time
                    dialogBinding.selectedDueDateText.text = "Due Date: ${dateFormat.format(selectedDueDate)}"
                },
                year, month, day
            ).show()
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.taskTitleInput.text.toString().trim()
                val description = dialogBinding.taskDescriptionInput.text.toString().trim()
                
                if (title.isNotBlank()) {
                    lifecycleScope.launch {
                        taskDao.taskDao().updateTask(
                            task.copy(
                                title = title,
                                description = description,
                                dueDate = selectedDueDate
                            )
                        )
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}