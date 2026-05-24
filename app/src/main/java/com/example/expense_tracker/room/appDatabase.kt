package com.example.expense_tracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [expenseEntity::class], version = 1, exportSchema = false)
abstract class appDatabase: RoomDatabase() {

    companion object{
        fun getInstance(context: Context): appDatabase{
            return Room.databaseBuilder(
                context,
                appDatabase::class.java,
                "Expense Database"
            ).build()
        }
    }
    abstract fun getExpenseDao() : expenseDao
}