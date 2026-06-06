package com.example.expense_tracker.UILayer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.time.Instant
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
){
  var type by remember {
      mutableStateOf("Income")
  }
    val expenseList by viewModel.getExpense().collectAsState(emptyList())

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
    expensePieChart(expenseChartFilter,expenseTotals,onValueChange={expenseChartFilter = it},dateChange = {start,end->
        expenseStartDate = start
        expenseEndDate = end
    })

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun incomePieChart(
    filterType: String,
    incomeList: Map<String,Int>,
    onValueChange1: (String) -> Unit = {},
    dateChange:(Long, Long) -> Unit,
) {
    var selectedLabel by remember {
        mutableStateOf("Not selected")
    }

    var selectedAmount by remember {
        mutableFloatStateOf(0f)
    }

    var selectedPercent by remember {
        mutableFloatStateOf(0f)
    }
    Column(

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            selectedPieChartInfo(selectedLabel, selectedAmount, selectedPercent)
            filterDropDown(filterType, onValueChange1, dateChange)
        }
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

                    chart.setOnChartValueSelectedListener(
                        object : OnChartValueSelectedListener{

                            override fun onValueSelected(e: Entry?, h: Highlight?) {
                                val pieEntry = e as PieEntry

                                val total = incomeList.values.sum()

                                val percent = pieEntry.value*100f/total

                                selectedLabel = pieEntry.label
                                selectedAmount = pieEntry.value
                                selectedPercent = percent

                            }

                            override fun onNothingSelected() {

                            }
                        }
                    )
                    val dataset = PieDataSet(entries, "")
                    dataset.colors = listOf(
                        android.graphics.Color.parseColor("#4E79A7"),
                        android.graphics.Color.parseColor("#59A14F"),
                        android.graphics.Color.parseColor("#F28E2B")
                    )
                   dataset.setDrawValues(false)
                    val data = PieData(dataset)
                    data.setValueFormatter(
                        PercentFormatter(chart)
                    )
                    chart.data = data
                    chart.setDrawEntryLabels(false)
                    chart.invalidate()
                }
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun expensePieChart(
    filterType: String,
    expenseTotals: Map<String,Int>,
    onValueChange: (String) -> Unit = {},
    dateChange:(Long,Long) -> Unit
) {
    var selectedLabel by remember {
        mutableStateOf("Not selected")
    }

    var selectedAmount by remember {
        mutableFloatStateOf(0f)
    }

    var selectedPercent by remember {
        mutableFloatStateOf(0f)
    }
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            selectedPieChartInfo(selectedLabel, selectedAmount, selectedPercent)
            filterDropDown(filterType, onValueChange, dateChange)
        }
            AndroidView(
                modifier = Modifier.fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = true
                    centerText = "Expense"

                    setDrawEntryLabels(false)
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
                dataSet.setDrawValues(false)
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


@RequiresApi(Build.VERSION_CODES.O)
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
    var showCustomRange by remember {
        mutableStateOf(false)
    }
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
                            else if(it=="Custom Range"){
                               showCustomRange = true
                            }
                        }
                    )
                }
            }
        }
        if(showCustomRange){
            customRangeFilter(
                dateChangeFun = dateChangeFun,
                onDismiss = {
                    showCustomRange = false
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customRangeFilter(
    dateChangeFun: (Long, Long) -> Unit,
    onDismiss:()-> Unit
){

    val startDateState = rememberDatePickerState()
    val endDateState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val start = startDateState.selectedDateMillis
                    val end = endDateState.selectedDateMillis

                        if(start!=null && end!=null && start<=end){
                            dateChangeFun(start,end)
                            onDismiss()
                        }
                    }
            ){
                Text(text="Apply FIlters")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text="Cancel")
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(text="Start Date")
                DatePicker(state = startDateState)
                Spacer(Modifier.height(16.dp))
                Text(text="End Date")
                DatePicker(state = endDateState)

            }
        }
    )
}
@Composable
fun selectedPieChartInfo(
    label:String,
    amount: Float,
    percent : Float
){
    Box(
        modifier = Modifier
            .padding(start = 24.dp)
            .border(1.dp,color = Color.Black, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth(0.3f)
    )
     {
         Column(
             modifier = Modifier
                 .padding(start = 12.dp,top = 6.dp,bottom = 6.dp)

         ) {

                 Text(
                     modifier = Modifier.padding(vertical = 4.dp),
                     fontWeight = FontWeight.Bold , text = "${label}")

                 Text(
                     fontWeight = FontWeight.Bold , text = "${amount}")

                 Text(
                     modifier = Modifier.padding(vertical = 4.dp),
                     fontWeight = FontWeight.Bold , text = "${percent}")

          }
      }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun previewScreen() {
    val navController = rememberNavController()
    var label by remember { mutableStateOf("Food") }
    var amount by remember { mutableFloatStateOf(2500f) }
    var percent by remember { mutableFloatStateOf(35f) }

//    Column(
//        modifier = Modifier.verticalScroll(rememberScrollState())
//    ) {
//
//        incomePieChart(
//            filterType = "This Month",
//            incomeList = mapOf(
//                "Salary" to 5000,
//                "Bonus" to 2000,
//                "Freelance" to 3000
//            ),
//            onValueChange1 = {},
//            dateChange = { _, _ -> }
//        )
//        expensePieChart(
//            filterType = "This Month",
//            expenseTotals = mapOf(
//                "Salary" to 5000,
//                "Bonus" to 2000,
//                "Freelance" to 3000
//            ),
//            onValueChange = {},
//            dateChange = { _, _ -> }
//        )
//    }
    MaterialTheme {
        customRangeFilter(dateChangeFun = { _, _ -> }, onDismiss = {})
    }
}