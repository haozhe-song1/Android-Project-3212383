package com.example.androidbighomework

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidbighomework.ui.theme.AndroidBigHomeworkTheme

class MainActivity : ComponentActivity() {

    private var searchText by mutableStateOf("")
    private var drinkList by mutableStateOf(listOf<Drink>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drinkList = DrinkData.getAll()
        enableEdgeToEdge()
        setContent {
            AndroidBigHomeworkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    private fun openDetail(id: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("drink_id", id)
        startActivity(intent)
    }

    @Composable
    private fun MainScreen() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Drink Guide ☕",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Search and filter drinks",
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },   // TODO: filter list by searchText next milestone
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Search drink names...") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                this@MainActivity,
                                "Coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add Drink")   // TODO: open AddDrinkActivity later
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(drinkList) { drink ->
                DrinkRow(drink = drink)
            }
        }
    }

    @Composable
    private fun DrinkRow(drink: Drink) {
        Card(
            onClick = { openDetail(drink.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = drink.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Calories " + drink.calories + " kcal  |  Sugar " + drink.sugar + " g",
                    fontSize = 14.sp
                )
            }
        }
    }
}
