package com.example.expense_tracker.UILayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracker.ExpenseViewModel
import com.example.expense_tracker.room.expenseEntity




// Hilt view model causes rendering issue therefore for preview remove viewmodel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddexpenseCard(
    id: Int,
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val options = listOf(
            "Income",
            "Expense"
        )
        val categ = listOf("Food","Shopping","Travel","Others")
        val categ2 = listOf("Salary","Bonus","Others")
            val previousItem = viewModel.getPreviousElement(id)


            var amount by remember {
                mutableStateOf(if(id!=-1) "${previousItem.amount}" else "")
            }
            var description by remember {
                mutableStateOf(if(id!=-1) "${previousItem.description}" else "")
            }
            var type by remember {
                mutableStateOf(if(id!=-1) "${previousItem.type}" else options[0])
            }
            var category by remember {
            mutableStateOf(if(id!=-1) "${previousItem.category}" else categ[0])
            }

        var expanded by remember {
            mutableStateOf(false)
        }
        var expanded1 by remember {
            mutableStateOf(false)
        }
        Box(
          modifier = Modifier
              .padding(12.dp)
        ){
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Type")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier.menuAnchor(
                        MenuAnchorType.PrimaryNotEditable
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { item->
                        DropdownMenuItem(
                           text = {
                               Text(item)
                           },
                            onClick = {
                                expanded = false
                                type = item
                            }
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(12.dp)
        ){
            ExposedDropdownMenuBox(
                expanded = expanded1,
                onExpandedChange = {
                    expanded1 = !expanded1
                }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Category")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded1)
                    },
                    modifier = Modifier.menuAnchor(
                        MenuAnchorType.PrimaryNotEditable
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded1,
                    onDismissRequest = {
                        expanded1 = false
                    }
                ) {
                    if(type=="Income") {
                        categ2.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(item)
                                },
                                onClick = {
                                    expanded1 = false
                                    type = item
                                }
                            )
                        }
                    }else{
                        categ.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(item)
                                },
                                onClick = {
                                    expanded1 = false
                                    type = item
                                }
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(12.dp)
        ){
            TextField(
                value = amount,
                onValueChange = {
                    amount = it;
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text("Amount")
                }
            )

        }
        Box(
            modifier = Modifier
                .padding(12.dp)
        ){
            TextField(
                value = description,
                onValueChange = {
                    description = it;
                },
                label = {
                    Text("Description")
                }
            )
        }
        Button(
            modifier = Modifier
                .padding(top=28.dp),
            onClick = {
                if(id!=-1){
                    viewModel.updateExpense(id,amount,type,category,description)
                    navController.navigate("Home")
                }
                else{
                    viewModel.addExpense(amount,type,category,description)
                    navController.navigate("Home")
                }
            }
        ) {
            Text("ADD+")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddScreenPreview(){
    val toUpdate = false;
    val item = expenseEntity(
        amount = 0,
        type = "",
        category = "",
        description = "",
        dateTime = 0
    )
    val navController = rememberNavController()
    AddexpenseCard(-1,navController)
}