package com.example.expense_tracker

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


     fun addExpense(amount: String,type: String,category: String,description: String) {
         if (amount.isBlank() || type.isBlank() || description.isBlank() || category.isBlank()) {
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
                 category = category,
                 description = description,
                 dateTime = System.currentTimeMillis()
             )
             viewModelScope.launch {
                 repository.addExpense(item)
                 _message.emit("Expense Added!")
             }
         }
     }

    fun updateExpense(id: Int,amount:String,type: String,category: String,description: String){
        if(amount.isBlank() || type.isBlank() || description.isBlank() || category.isBlank()){
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
            category = category,
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
               category = "",
               description = "",
               dateTime = 0)
        viewModelScope.launch {
            item = repository.getElement(id)
        }
        return item
    }

    fun filter(filter:String,selectedCategory:String,expenseList:List<expenseEntity>):List<expenseEntity>{
        var result = expenseList
        if(selectedCategory!=""){
            result = expenseList.filter { item->
                item.category == selectedCategory
            }
        }
        if(filter == "Income" || filter=="Expense") {
            result = result.filter {
                it.type == filter

            }
        }
        else if(filter == "High to Low"){
            result = result.sortedByDescending {
                it.amount
            }
        }
        else if(filter == "Low to High"){
            result = result.sortedBy {
                it.amount
            }
        }
        else{
            return expenseList
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterByMonth(month:Int, expenseList:List<expenseEntity>):List<expenseEntity>{
        val calendar = Calendar.getInstance()
        return expenseList.filter {

            calendar.timeInMillis = it.dateTime
            calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == LocalDate.now().year
        }
    }

    fun totalBalance(expenseList: List<expenseEntity>): Int {
        var total = 0
        expenseList.forEach { item ->
            if (item.type == "Income") {
                total += item.amount
            } else {
                total -= item.amount
            }
        }
       return total
    }


    fun searchByValue(searchValue:String,expenseList: List<expenseEntity>):List<expenseEntity>{
        if(searchValue==""){
            return expenseList
        }
        else {
            return expenseList.filter { item ->
                item.description.contains(searchValue)
            }
        }
    }
}