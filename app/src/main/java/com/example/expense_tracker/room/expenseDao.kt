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

    @Update
    suspend fun updateExpense(expenseEntity: expenseEntity)

    @Query("DELETE FROM expenseEntity WHERE id=:id")
    suspend fun deleteExpense(id:Int)

    @Query("SELECT * FROM expenseEntity")
    fun getAllExpense(): Flow<List<expenseEntity>>

    @Query("SELECT * FROM expenseEntity WHERE id=:id")
    suspend fun getElement(id: Int?): expenseEntity

}