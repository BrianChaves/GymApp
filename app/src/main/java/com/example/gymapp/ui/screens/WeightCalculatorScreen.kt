package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import com.example.gymapp.ui.TopBar

data class Weight(val value: Double, val name: String)

val barWeight = Weight(45.0, "Barra")
val weights = listOf(
    Weight(45.0, "Disco 45 lbs"),
    Weight(35.0, "Disco 35 lbs"),
    Weight(25.0, "Disco 25 lbs"),
    Weight(15.0, "Disco 15 lbs"),
    Weight(10.0, "Disco 10 lbs"),
    Weight(5.0, "Disco 5 lbs"),
    Weight(2.5, "Disco 2.5 lbs")
)

fun calculateWeightDistribution(targetWeight: Double): Map<String, Int> {
    var remainingWeight = targetWeight - barWeight.value
    val result = mutableMapOf<String, Int>()

    if (remainingWeight < 0) {
        return result
    }

    for (weight in weights) {
        val count = (remainingWeight / (weight.value * 2)).toInt()
        if (count > 0) {
            result[weight.name] = count * 2
            remainingWeight -= count * weight.value * 2
        }
    }
    return result
}

@Composable
fun WeightCalculatorScreen() {
    var inputWeight by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Map<String, Int>?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputWeight,
            onValueChange = { inputWeight = it },
            label = { Text(text = "Enter desired weight (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val weight = inputWeight.toDoubleOrNull()
                if (weight != null && weight >= 45.0) {
                    result = calculateWeightDistribution(weight)
                    errorMessage = ""
                } else {
                    result = null
                    errorMessage = "Please enter a valid weight greater than or equal to 45 lbs."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
        result?.let { WeightDistributionList(result = it) }
    }
}

@Composable
fun WeightDistributionList(result: Map<String, Int>) {
    Text("Bar: 45 lbs")
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(result.entries.toList()) { entry ->
            Text("Plate: ${entry.key} lbs: ${entry.value}")
            Divider()
        }
    }
}