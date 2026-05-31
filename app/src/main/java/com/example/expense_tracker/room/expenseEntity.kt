package com.example.expense_tracker.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class expenseEntity (
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    val amount : Int,
    val type : String,
    val category : String,
    val description : String,
    val dateTime : Long
)