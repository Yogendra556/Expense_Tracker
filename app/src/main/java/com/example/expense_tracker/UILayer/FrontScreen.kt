package com.example.expense_tracker.UILayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracker.ExpenseViewModel
import com.example.expense_tracker.room.expenseEntity

@Composable
fun frontScreenRoute(
    viewModel: ExpenseViewModel = viewModel()
){
    val item by viewModel.getExpense().collectAsState(emptyList())
    FrontScreen(item)
}

@Composable
fun FrontScreen(
   expenseList : List<expenseEntity>
){
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Box(

            ) {
                Text("Month")
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    items(expenseList){item->
                        expenseCard(item)
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(bottom = 16.dp, end = 10.dp,top = 0.dp, start = 0.dp)
                        .align(Alignment.BottomEnd)
                    ,
                    onClick = {},

                ) {
                    Text(modifier = Modifier, fontSize = 36.sp , fontWeight = FontWeight.SemiBold, text = "+")
                }
            }
        }


}

@Composable
fun expenseCard(
    item : expenseEntity
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
         Text("${item.amount}")
         Text("${item.type}")
         Text("${item.dateTime}")
    }

}
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
    FrontScreen(listOf(item1,item2))
}