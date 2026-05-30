package com.example.expense_tracker.UILayer

import android.os.Build
import android.service.autofill.OnClickAction
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracker.ExpenseViewModel
import com.example.expense_tracker.room.expenseEntity
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.exp
import kotlin.math.log

@Composable
fun frontScreenRoute(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
){
    val item by viewModel.getExpense().collectAsState(emptyList())
    FrontScreen(navController,item)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrontScreen(
    navController: NavController,
    expenseList : List<expenseEntity>,
    viewModel: ExpenseViewModel = hiltViewModel()
){

       var filteredList by remember(expenseList){
           mutableStateOf(expenseList)
       }
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
        ) {
            Box(
               modifier = Modifier
                   .padding(top = 12.dp,start = 12.dp,end = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var month by remember {
                            mutableStateOf(LocalDate.now().month.value)
                        }
                        IconButton(onClick = {
                            if(month==0) month = 11 else month--
                            filteredList = viewModel.filterByMonth(month,expenseList)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month"
                            )
                        }
                        Text(
                            fontSize = 18.sp,
                            text = "${java.time.Month.of(month+1).name}"
                        )

                        IconButton(onClick = {
                            if(month==11) month = 0 else month++
                            filteredList = viewModel.filterByMonth(month,expenseList)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Previous Month"
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
//                        Text(
//                            modifier = Modifier
//                                .align(Alignment.End)
//                                .padding(end = 12.dp),
//                            text = "filter"
//                        )
                        var expanded by remember{
                            mutableStateOf(false)
                        }
                        var filter by remember{
                            mutableStateOf("")
                        }
                        ExposedDropdownMenuBox(
                             expanded = expanded,
                             onExpandedChange = {
                                 expanded = !expanded
                             },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 12.dp),
                        ) {
                            OutlinedTextField(
                                value = "Filter",
                                 onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                },
                                modifier = Modifier.menuAnchor(
                                    MenuAnchorType.PrimaryNotEditable
                                ).fillMaxWidth(0.5f)
                            )
                            ExposedDropdownMenu(
                               expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ){
                                listOf("Income","Expense","High to Low","Low to High").forEach { item->
                                    DropdownMenuItem(
                                        text = {
                                            Text(item)
                                        },
                                        onClick = {
                                            expanded = false
                                            filter = item
                                            filteredList = viewModel.filter(filter,expenseList)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    items(filteredList){item->
                        expenseCard(item,navController)
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(bottom = 16.dp, end = 10.dp,top = 0.dp, start = 0.dp)
                        .align(Alignment.BottomEnd)
                    ,
                    onClick = {
                        navController.navigate("Add/-1")
                    },

                ) {
                    Text(modifier = Modifier, fontSize = 36.sp , fontWeight = FontWeight.SemiBold, text = "+")
                }
            }
        }
}

@Composable
fun expenseCard(
    item : expenseEntity,
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
){
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${item.amount}")
            Text("${item.type}")
            Text("${item.dateTime}")
            Text("Update", modifier = Modifier.clickable(onClick = {
                navController.navigate("Add/${item.id}")
            }))
        }
        Row(
           horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text("${item.description}")
            }
            Box(
                modifier = Modifier
            ){
                IconButton(onClick = {
                    viewModel.deleteExpense(item.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun FrontScreenView(){
    val item1 = expenseEntity(
        amount = 100,
        type = "uinc",
        description = "njejkd",
        dateTime = 7389
    )
    val item2 = expenseEntity(
        amount = 100,
        type = "uinc",
        description = "njejkd",
        dateTime = 7389
    )
    val navController = rememberNavController()
    FrontScreen(navController,listOf(item1,item2))
}