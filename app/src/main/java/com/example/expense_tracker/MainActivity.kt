package com.example.expense_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expense_tracker.UILayer.AddexpenseCard
import com.example.expense_tracker.UILayer.AnalyticScreen
import com.example.expense_tracker.UILayer.frontScreenRoute
import com.example.expense_tracker.ui.theme.Expense_TrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar() {
                           navBar(navController)
                        }
                    }
                ) {paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = "Home"
                    ) {

                        composable("Home") {
                            frontScreenRoute(navController)
                        }
                        composable("Analytics") {
                            AnalyticScreen(navController)
                        }
                        composable(
                            "Add/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1

                            AddexpenseCard(
                                id,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun navBar(navController: NavController){
    Row {

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("Home")
            },
            icon = { Text("🏠") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("Analytics")
            },
            icon = { Text("📊") },
            label = { Text("Analytics") }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun expensePreview(){
    val navController = rememberNavController()
    NavigationBar {
        navBar(navController)
    }
}




