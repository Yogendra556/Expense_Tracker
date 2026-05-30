package com.example.expense_tracker

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.repository.ExpenseRepository
import com.example.expense_tracker.room.expenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel(){

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()


     fun addExpense(amount: String,type: String,description: String) {
         if (amount.isBlank() || type.isBlank() || description.isBlank()) {
             viewModelScope.launch {
                 _message.emit("Please fill all fields")
             }
             return
         }
         val amountInt = amount.toIntOrNull()
         if (amountInt == null) {
             viewModelScope.launch {
                 _message.emit("Invalid amount")
             }
             return
         } else {
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

    fun getPreviousElement(id:Int?):expenseEntity{
           var item = expenseEntity(
               amount = 0,
               type = "",
               description = "",
               dateTime = 0)
        viewModelScope.launch {
            item = repository.getElement(id)
        }
        return item
    }

    fun filter(filter:String,expenseList:List<expenseEntity>):List<expenseEntity>{

        if(filter == "Income" || filter=="Expense") {
            return expenseList.filter {
                it.type == filter

            }
        }
        else if(filter == "High to Low"){
            return expenseList.sortedByDescending {
                it.amount
            }
        }
        else if(filter == "Low to High"){
            return expenseList.sortedBy {
                it.amount
            }
        }
        else{
            return expenseList
        }
    }

    fun filterByMonth(month:Int,expenseList:List<expenseEntity>):List<expenseEntity>{
        val calendar = Calendar.getInstance()
        return expenseList.filter {

            calendar.timeInMillis = it.dateTime
            calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == LocalDate.now().year
        }
    }
}