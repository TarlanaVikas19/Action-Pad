package com.example.managertask.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val dueDate: Date,
    val isCompleted: Boolean = false,
    val isFavourite: Boolean = false,
    val createdAt: Date = Date()
) 