package com.example.datediffcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.datediffcalculator.ui.theme.DateDiffCalculatorTheme
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import com.example.datediffcalculator.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DateDiffCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    doubleDatePickerDialog()
                }
            }
        }
    }
}


fun getFormattedDate(timeInMillis: Long): String{
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(calender.timeInMillis)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun doubleDatePickerDialog(){

    var datePickerVisibility = remember {mutableStateOf(false)}
    val dateRangePickerState = rememberDateRangePickerState()
    val confirmEnabled = remember {
        derivedStateOf { dateRangePickerState.selectedStartDateMillis != null
                && dateRangePickerState.selectedEndDateMillis != null }
    }
    val selectedStartDateMillis = remember {
        derivedStateOf {
            dateRangePickerState.selectedStartDateMillis
        }
    }
    val selectedEndDateMillis = remember {
        derivedStateOf {
            dateRangePickerState.selectedEndDateMillis
        }
    }
    Column (modifier = Modifier.fillMaxHeight(1f)) {
        HeaderRowWithOptionsDescription()
        Button(onClick = {
                datePickerVisibility.value = !(datePickerVisibility.value)
                },
            Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
            Text(text = "Wybierz daty", fontSize = 22.sp)
        }
        if (datePickerVisibility.value) {
            DatePickerDialog(
                onDismissRequest = {
                    datePickerVisibility.value = false

                },
                confirmButton = {
                    //datePickerVisibility = false
                                TextButton(onClick = {
                                    datePickerVisibility.value = false
                                },
                                    enabled = confirmEnabled.value) {
                                    Text(text="OK")
                                }
                },
                dismissButton = {
                                TextButton(onClick = { datePickerVisibility.value = false
                                            }) {
                                            Text(text = "Wróć")
                                }
                }
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    headline = {
                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(10.dp,0.dp,10.dp,0.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly){
                            TextButton(onClick ={
                                datePickerVisibility.value = false
                            },
                                enabled = confirmEnabled.value) {
                                Text(text="OK", fontSize = 18.sp)
                            }
                            TextButton(onClick = { datePickerVisibility.value = false
                            }) {
                                Text(text = "Wróć", fontSize = 18.sp)
                            }
                        }
                    }
                    )
            }
        }
        if ((selectedEndDateMillis.value != null
                && selectedEndDateMillis.value != null) && !datePickerVisibility.value
        ) {
            var startDateInt = ((
                    selectedStartDateMillis.value?.div(24) ?: null)
                ?.div(3600) ?: null)?.div(1000) ?: null
            var endDateInt = ((
                selectedEndDateMillis.value?.div(24) ?: null)
                ?.div(3600) ?: null)?.div(1000) ?: null
            val startDate = dateRangePickerState.selectedStartDateMillis?.let { getFormattedDate(it) }
            val endDate = dateRangePickerState.selectedEndDateMillis?.let { getFormattedDate(it) }
            val durationInDays = startDateInt?.let { endDateInt?.minus(it) }
            val durationInDaysText : String
            val durationInHoursInt = durationInDays?.times(24)
            val durationInMinutesInt = durationInHoursInt?.times(60)
            val durationInSecondsInt = durationInMinutesInt?.times(60)
            val durationInHoursText = "$durationInHoursInt godzin"
            val durationInMinutesText = "$durationInMinutesInt minut"
            val durationInSecondsText = "$durationInSecondsInt sekund"
            if (durationInDays == 1.toLong()) {
                durationInDaysText = "1 dzień"
            } else {
                durationInDaysText = "$durationInDays dni"
            }
            val resultHeaderFontSize = 24.sp
            Column(modifier = Modifier.fillMaxHeight(0.75f)){
                Row(modifier = Modifier.background(Color.LightGray).fillMaxWidth(1f)){
                    Text(text = "Ilość dni między datami: ", fontSize = resultHeaderFontSize)
                    }
                Row(modifier = Modifier.background(Color.LightGray).fillMaxWidth(1f)){
                    Text(text = "$startDate ", fontSize = resultHeaderFontSize)
                    Text(text = "oraz ", fontSize = resultHeaderFontSize)
                    Text(text = "$endDate", fontSize = resultHeaderFontSize)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .fillMaxWidth(1f)
                        .padding(0.dp,15.dp,0.dp,0.dp)){
                    Column{
                        Text(
                            text = durationInDaysText,
                            fontSize = 35.sp,
                            color = Color.White)
                        Text(
                            text = durationInHoursText,
                            fontSize = 35.sp,
                            color = Color.White)
                        Text(
                            text = durationInMinutesText,
                            fontSize = 35.sp,
                            color = Color.White)
                        Text(
                            text = durationInSecondsText,
                            fontSize = 35.sp,
                            color = Color.White)
                    }

                }
            }
        }
    }
}

@Composable
fun HeaderRowWithOptionsDescription() {
    Column {
        var isHelpRowVisible by remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.DarkGray)
                .padding(3.dp, 7.dp, 3.dp, 0.dp)
        ) {
            Text(text = "Kalkulator różnicy dat", fontSize = 35.sp, color = Color.White)
            IconButton(onClick = {
                isHelpRowVisible = !isHelpRowVisible
                }
            ,
                modifier = Modifier) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Help button",
                    tint = Color.White
                )
            }
        }
        if (isHelpRowVisible){
            Row(modifier = Modifier.background(
                color = Color.Gray)
            ){
                Text(
                    text="Kalkulator służy do obliczania ilości dni między wybranymi datami od 01.01.1900 do 31.12.2100",
                    color = Color.White
                )

            }
        }

    }
}

@Preview
@Composable
fun PreviewDoubleDatePickerDialog() {
    doubleDatePickerDialog()
}

@Preview
@Composable
fun PreviewHeaderRowWithOptionsDescription() {
    HeaderRowWithOptionsDescription()
}