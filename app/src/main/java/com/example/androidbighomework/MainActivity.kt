package com.example.androidbighomework

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity(), SensorEventListener {

    private var searchText by mutableStateOf("")
    private var sortIndex by mutableStateOf(0)
    private var minStars by mutableStateOf(0)
    private var maxStars by mutableStateOf(0)
    private var drinkList by mutableStateOf(listOf<Drink>())

    private val sortNames = listOf(
        "Default order",
        "Calories: low to high",
        "Calories: high to low",
        "Sugar: low to high",
        "Sugar: high to low"
    )
    private val starNames = listOf("Any", "1 star", "2 stars", "3 stars", "4 stars", "5 stars")

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastShakeTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrinkGuideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColors.Cream
                ) {
                    MainScreen()
                }
            }
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        updateList()
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        // A large combined acceleration on all axes counts as one shake.
        val power = x * x + y * y + z * z
        val now = System.currentTimeMillis()
        if (power > 22f * 22f && now - lastShakeTime > 1800) {
            lastShakeTime = now
            showRandomDrink()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Search text, star range and sort order can be combined.
    private fun updateList() {
        val text = searchText.trim()
        val drinks = DrinkData.getAll(this)
        val result = mutableListOf<Drink>()

        for (drink in drinks) {
            val matchesText = drink.name.contains(text, ignoreCase = true) ||
                drink.description.contains(text, ignoreCase = true) ||
                drink.steps.contains(text, ignoreCase = true)
            var matchesStars = true
            if (minStars > 0 || maxStars > 0) {
                matchesStars = drink.rating > 0 &&
                    (minStars == 0 || drink.rating >= minStars) &&
                    (maxStars == 0 || drink.rating <= maxStars)
            }
            if (matchesText && matchesStars) {
                result.add(drink)
            }
        }

        if (sortIndex == 1) {
            result.sortBy { it.calories }
        } else if (sortIndex == 2) {
            result.sortByDescending { it.calories }
        } else if (sortIndex == 3) {
            result.sortBy { it.sugar }
        } else if (sortIndex == 4) {
            result.sortByDescending { it.sugar }
        }
        drinkList = result
    }

    private fun openDetail(id: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("drink_id", id)
        startActivity(intent)
    }

    private fun showRandomDrink() {
        // Shake picks from all drinks, ignoring the current list filters.
        val drinks = DrinkData.getAll(this)
        if (drinks.isEmpty()) {
            Toast.makeText(this, "No drinks to recommend", Toast.LENGTH_SHORT).show()
            return
        }
        val index = Random.nextInt(drinks.size)
        openDetail(drinks[index].id)
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
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Green
                )
            }
            item {
                Text(
                    text = "Search and filter drinks; shake your phone for a random pick",
                    fontSize = 13.sp,
                    color = AppColors.Green
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = searchText,
                    onValueChange = { newText ->
                        searchText = newText
                        updateList()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Search drink names or keywords") },
                    singleLine = true
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Sort by", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
                for (i in sortNames.indices) {
                    val isSelected = sortIndex == i
                    Button(
                        onClick = {
                            sortIndex = i
                            updateList()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) AppColors.Green else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = sortNames[i])
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Min rating", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
                StarRow(stars = minStars, onPick = { picked ->
                    minStars = picked
                    updateList()
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Max rating", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
                StarRow(stars = maxStars, onPick = { picked ->
                    maxStars = picked
                    updateList()
                })
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { startActivity(Intent(this@MainActivity, EditDrinkActivity::class.java)) },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(text = "Add Drink")
                    }
                    Button(
                        onClick = { showRandomDrink() },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text(text = "Surprise Me")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { startActivity(Intent(this@MainActivity, MoreActivity::class.java)) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "More")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (drinkList.isEmpty()) {
                item {
                    Text(
                        text = "No drinks match your filters",
                        fontSize = 16.sp,
                        color = AppColors.Green,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    )
                }
            }
            items(drinkList) { drink ->
                DrinkRow(drink = drink)
            }
        }
    }

    @Composable
    private fun StarRow(stars: Int, onPick: (Int) -> Unit) {
        Row(modifier = Modifier.fillMaxWidth()) {
            for (i in starNames.indices) {
                val isSelected = stars == i
                Button(
                    onClick = { onPick(i) },
                    modifier = Modifier.weight(1f).padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) AppColors.Green else Color.LightGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = starNames[i], fontSize = 11.sp)
                }
            }
        }
    }

    @Composable
    private fun DrinkRow(drink: Drink) {
        var ratingText = "Not rated"
        if (drink.rating > 0) {
            if (drink.rating == 1) {
                ratingText = "1 star"
            } else {
                ratingText = drink.rating.toString() + " stars"
            }
        }
        var typeText = "Preset drink"
        if (drink.isCustom) {
            typeText = "Added by me"
        }
        Card(
            onClick = { openDetail(drink.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = drink.name, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Calories " + drink.calories + " kcal  |  Sugar " + drink.sugar + " g\n" +
                        ratingText + "  ·  " + typeText,
                    fontSize = 14.sp,
                    color = AppColors.Green
                )
            }
        }
    }
}
