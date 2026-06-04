package com.example.expense_tracker.UILayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracker.ExpenseViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.time.Instant
import java.time.ZoneId


@Composable
fun AnalyticScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
){
  var type by remember {
      mutableStateOf("Income")
  }
    val expenseList by viewModel.getExpense().collectAsState(emptyList())
    var barChartFilter by remember {
        mutableStateOf("Days")
    }
    var incomeChartFilter by remember {
        mutableStateOf("This Month")
    }
    var expenseChartFilter by remember {
        mutableStateOf("This Month")
    }
    var incomeStartDate by remember {
        val currentTime = System.currentTimeMillis()
        mutableStateOf(Instant
            .ofEpochMilli(currentTime)
            .atZone(ZoneId.systemDefault())
            .minusMonths(1)
            .toInstant()
            .toEpochMilli())
    }
    var expenseStartDate by remember {
        val currentTime = System.currentTimeMillis()
        mutableStateOf(Instant
            .ofEpochMilli(currentTime)
            .atZone(ZoneId.systemDefault())
            .minusMonths(1)
            .toInstant()
            .toEpochMilli())
    }
    var incomeEndDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    var expenseEndDate by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    var incomeList = remember(expenseList){
        viewModel.filterByDate("Income",expenseList,incomeStartDate,incomeEndDate)
    }
    var expenseTotals = remember(expenseList) {
        viewModel.filterByDate("Expense",expenseList,expenseStartDate,expenseEndDate)
    }

    incomePieChart(incomeChartFilter,incomeList,onValueChange1 = {incomeChartFilter = it}, dateChange = {start,end->
        incomeStartDate = start
        incomeEndDate = end
    })
    expensePieChart(expenseChartFilter,expenseTotals,onValueChange={expenseChartFilter = it})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun incomePieChart(
    filterType: String,
    incomeList: Map<String,Int>,
    onValueChange1: (String) -> Unit = {},
    dateChange:(Long, Long) -> Unit
) {
    Column(

    ) {
        filterDropDown(filterType,onValueChange1,dateChange)
        Box() {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context ->
                    PieChart(context).apply {

                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        centerText = "Income"

                        setEntryLabelTextSize(12f)
                        setEntryLabelColor(android.graphics.Color.BLACK)
                        setUsePercentValues(true)
                        setCenterTextSize(18f)
                        setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD)
                    }
                },
                update = { chart ->
                    val entries = incomeList.map { it ->
                        PieEntry(
                            it.value.toFloat(),
                            it.key
                        )
                    }

                    val dataset = PieDataSet(entries, "")
                    dataset.colors = listOf(
                        android.graphics.Color.parseColor("#4E79A7"),
                        android.graphics.Color.parseColor("#59A14F"),
                        android.graphics.Color.parseColor("#F28E2B")
                    )
                    dataset.valueTextSize = 12f
                    val data = PieData(dataset)
                    data.setValueFormatter(
                        PercentFormatter(chart)
                    )
                    chart.data = data
                    chart.invalidate()
                }
            )
        }
    }
}
@Composable
fun expensePieChart(
    filterType: String,
    expenseTotals: Map<String,Int>,
    onValueChange: (String) -> Unit = {}
) {
    Column() {
//        filterDropDown(filterType,onValueChange)
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = true
                    centerText = "Expense"

                    setEntryLabelTextSize(12f)
                    setEntryLabelColor(android.graphics.Color.BLACK)
                    setUsePercentValues(true)
                    setCenterTextSize(18f)
                    setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD)
                }
            },
            update = { chart ->
                val entries = expenseTotals.map {
                    PieEntry(
                        it.value.toFloat(),
                        it.key
                    )
                }
                val dataSet = PieDataSet(entries, "")
                dataSet.colors = listOf(
                    android.graphics.Color.parseColor("#4E79A7"),
                    android.graphics.Color.parseColor("#59A14F"),
                    android.graphics.Color.parseColor("#F28E2B"),
                    android.graphics.Color.parseColor("#E15759")
                )
                dataSet.valueTextSize = 12f
                val data = PieData(dataSet)
                data.setValueFormatter(
                    PercentFormatter(chart)
                )
                chart.data = data
                chart.invalidate()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun filterDropDown(
    filterType: String,
    onValueChangeFun: (String) -> Unit = {},
    dateChangeFun : (Long,Long) -> Unit
){
    var expanded by remember {
        mutableStateOf(false)
    }
    val filterItems = listOf("This Month","Previous Month","Custom Range")
    Box(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = filterType,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Filter")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier.menuAnchor(
                    MenuAnchorType.PrimaryNotEditable
                ).align(Alignment.CenterEnd).fillMaxWidth(0.5f),

            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                filterItems.forEach {
                    DropdownMenuItem(
                        text = {
                            Text("${it}")
                        },
                        onClick = {
                            expanded = false
                            onValueChangeFun(it)
                            if(it=="Previous Month"){
                               val currentTime = System.currentTimeMillis()
                                val end = Instant
                                    .ofEpochMilli(currentTime)
                                    .atZone(ZoneId.systemDefault())
                                    .minusMonths(1)
                                    .toInstant()
                                    .toEpochMilli()

                                val start = Instant
                                    .ofEpochMilli(currentTime)
                                    .atZone(ZoneId.systemDefault())
                                    .minusMonths(2)
                                    .toInstant()
                                    .toEpochMilli()

                                dateChangeFun(start,end)
                            }
                        }
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun previewScreen(){
    val navController = rememberNavController()
    val map = mapOf("Salary" to 1000,"Bonus" to 2000,"Others" to 3000)
//    incomePieChart("This Month",map,{},{})
    val map1 = mapOf("Travel" to 1299,"Food" to 3827,"Shopping" to 6738,"Other" to 2678)
//    expensePieChart(map1)
}