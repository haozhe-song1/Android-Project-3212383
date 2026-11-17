package com.example.androidbighomework

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class DetailActivity : ComponentActivity() {

    private var drinkId = 0
    private var drink by mutableStateOf<Drink?>(null)
    private var rating by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drinkId = intent.getIntExtra("drink_id", 0)
        setContent {
            DrinkGuideTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Cream) {
                    DetailScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadDrink()
    }

    private fun reloadDrink() {
        val found = DrinkData.getById(this, drinkId)
        if (found == null) {
            finish()
            return
        }
        drink = found
        rating = found.rating
    }

    private fun saveRating(stars: Int) {
        val found = drink
        if (found == null) {
            return
        }
        rating = stars
        val dao = DrinkDatabase.getDatabase(this).drinkDao()
        dao.saveRating(DrinkRating(DrinkData.getKey(found), stars))
        Toast.makeText(this, "Rating saved", Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun DetailScreen() {
        val current = drink
        if (current == null) {
            Text(text = "Loading...", modifier = Modifier.padding(20.dp))
            return
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(text = current.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Calories: " + current.calories + " kcal\n" +
                    "Sugar: " + current.sugar + " g\n" +
                    "Caffeine: " + current.caffeine + " mg",
                fontSize = 17.sp,
                color = AppColors.Green
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "About This Drink", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = current.description, fontSize = 16.sp, color = AppColors.Green)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "How to Make It", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = current.steps, fontSize = 16.sp, color = AppColors.Green)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "My Rating (1-5 Stars)", fontSize = 18.sp, color = AppColors.Green)
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 1..5) {
                    TextButton(onClick = { saveRating(i) }) {
                        if (i <= rating) {
                            Text(text = "★", fontSize = 36.sp, color = AppColors.StarYellow)
                        } else {
                            Text(text = "☆", fontSize = 36.sp, color = Color.LightGray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (current.isCustom) {
                Button(
                    onClick = {
                        val intent = Intent(this@DetailActivity, EditDrinkActivity::class.java)
                        intent.putExtra("drink_id", drinkId)
                        startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Edit This Drink")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Back to List")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { startActivity(Intent(this@DetailActivity, MoreActivity::class.java)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "More")
            }
        }
    }
}
