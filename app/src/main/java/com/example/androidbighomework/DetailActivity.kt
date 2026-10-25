package com.example.androidbighomework

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidbighomework.ui.theme.AndroidBigHomeworkTheme

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getIntExtra("drink_id", 0)
        enableEdgeToEdge()
        setContent {
            AndroidBigHomeworkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DetailScreen(drinkId = drinkId)
                }
            }
        }
    }

    @Composable
    private fun DetailScreen(drinkId: Int) {
        val current = DrinkData.getById(drinkId)
        if (current == null) {
            Text(text = "Drink not found", modifier = Modifier.padding(20.dp))
            return
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = current.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Calories: " + current.calories + " kcal\n" +
                    "Sugar: " + current.sugar + " g\n" +
                    "Caffeine: " + current.caffeine + " mg",
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "About This Drink", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = current.description, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "How to Make It", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = current.steps, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(24.dp))
            // TODO: star rating buttons, will save to Room in a later milestone
            Text(text = "☆ ☆ ☆ ☆ ☆", fontSize = 28.sp, color = Color(0xFFF5A623))
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Back to List")
            }
        }
    }
}
