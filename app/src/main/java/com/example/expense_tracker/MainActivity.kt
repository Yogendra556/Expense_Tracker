package com.example.expense_tracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expense_tracker.repository.ExpenseRepository
import com.example.expense_tracker.room.appDatabase
import com.example.expense_tracker.room.expenseEntity
import com.example.expense_tracker.ui.theme.Expense_TrackerTheme

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Expense_TrackerTheme {
                // For room and repo layer testing
//                val db = appDatabase.getInstance(this)
//                val dao = db.getExpenseDao()
//                val repo = ExpenseRepository(dao)
//                val item = expenseEntity(amount = 100, description = "hvhdhbc" , type = "jcnjs" , dateTime = System.currentTimeMillis())
//                LaunchedEffect(Unit) {
//                    repo.addExpense(item)
//                    repo.addExpense(item)
//                    repo.getExpenseList().collect { expense->
//                        Log.d("tag",expense.toString())
//                    }
//                }

            }
        }
    }
}

