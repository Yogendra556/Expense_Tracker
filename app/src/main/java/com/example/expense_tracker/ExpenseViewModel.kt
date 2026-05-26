package com.example.expense_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.repository.ExpenseRepository
import com.example.expense_tracker.room.expenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel(){

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()


     fun addExpense(amount: String,type: String,description: String){
        if(amount.isBlank() || type.isBlank() || description.isBlank()){
              viewModelScope.launch {
                  _message.emit("Please fill all fields")
              }
            return
        }
         val amountInt = amount.toInt()
         val item = expenseEntity(
             amount = amountInt,
             type = type,
             description = description,
             dateTime = System.currentTimeMillis()
         )
         viewModelScope.launch {
             repository.addExpense(item)
             _message.emit("Expense Added!")
         }
    }

    fun updateExpense(id: Int,amount:String,type: String,description: String){
        if(amount.isBlank() || type.isBlank() || description.isBlank()){
            viewModelScope.launch {
                _message.emit("Please fill all fields")
            }
            return
        }
        val amountInt = amount.toInt()
        val item = expenseEntity(
            id = id,
            amount = amountInt,
            type = type,
            description = description,
            dateTime = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.updateExpense(item)
            _message.emit("Expense Updated!")
        }
    }

    fun deleteExpense(id:Int){
        viewModelScope.launch {
            repository.deleteExpense(id)
            _message.emit("Expense deleted")
        }
    }

    fun getExpense(): Flow<List<expenseEntity>>{
        return repository.getExpenseList()
    }
}