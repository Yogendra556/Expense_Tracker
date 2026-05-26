package com.example.expense_tracker.UILayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun AddexpenseCard(){
    Column() {
        var amount by remember {
            mutableStateOf("")
        }
        var type by remember {
            mutableStateOf("")
        }
        var description by remember {
            mutableStateOf("")
        }
        Box(){
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
        Box(){
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
        Box(){
            TextField(
                value = type,
                onValueChange = {
                    type = it;
                },
                label = {
                    Text("Amount")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddScreenPreview(){
    AddexpenseCard()
}