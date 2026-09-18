package com.example.listycity3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }

    // null = form is in "add" mode. Non-null = form is editing that city.
    var selectedCity by remember { mutableStateOf<City?>(null) }
    val isEditing = selectedCity != null

    Column(
        // imePadding() shrinks this Column's available height by the height
        // of the on-screen keyboard whenever it's visible, instead of letting
        // the keyboard draw on top of the content. That keeps the input
        // fields above visible and un-covered while typing, which was the
        // issue flagged in earlier feedback.
        modifier = modifier.imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text(if (isEditing) "Updated City" else "City") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = newProvinceName,
                onValueChange = { newProvinceName = it },
                label = { Text(if (isEditing) "Updated Province" else "Province") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.padding(vertical = 12.dp),
                onClick = {
                    if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                        val currentSelection = selectedCity
                        val updated = City(name = newCityName, province = newProvinceName)

                        if (currentSelection == null) {
                            onAddCity(updated)
                        } else {
                            onUpdateCity(currentSelection, updated)
                            selectedCity = null
                        }

                        newCityName = ""
                        newProvinceName = ""
                    }
                }
            ) {
                Text(if (isEditing) "Update City" else "Add City")
            }
        }

        LazyColumn {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        selectedCity = city
                        newCityName = city.name
                        newProvinceName = city.province
                    }
                )

                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(
    city: City,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> }
        )
    }
