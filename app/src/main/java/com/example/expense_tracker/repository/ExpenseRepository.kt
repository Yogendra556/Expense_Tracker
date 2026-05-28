package com.example.expense_tracker.repository

import com.example.expense_tracker.room.expenseDao
import com.example.expense_tracker.room.expenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val ExpenseDao : expenseDao
) {

    suspend fun addExpense(expenseEntity: expenseEntity){
        ExpenseDao.addExpense(expenseEntity)
    }

    suspend fun updateExpense(expenseEntity: expenseEntity){
        ExpenseDao.updateExpense(expenseEntity)
    }

    suspend fun deleteExpense(id:Int){
        ExpenseDao.deleteExpense(id)
    }

    fun getExpenseList(): Flow<List<expenseEntity>> {
        return ExpenseDao.getAllExpense()
    }

    fun getElement(id:Int?):expenseEntity {
        return ExpenseDao.getElement(id)
    }


}