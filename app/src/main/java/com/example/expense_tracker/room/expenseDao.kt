package com.example.expense_tracker.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface expenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expenseEntity: expenseEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpense(expenseEntity: expenseEntity)

    @Query("DELETE FROM expenseEntity WHERE id=:id")
    suspend fun deleteExpense(id:Int)

    @Query("SELECT * FROM expenseEntity")
    suspend fun getAllExpense(): Flow<List<expenseEntity>>

}