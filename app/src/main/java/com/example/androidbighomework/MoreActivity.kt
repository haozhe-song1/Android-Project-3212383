package com.example.androidbighomework

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrinkGuideTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Cream) {
                    MoreScreen()
                }
            }
        }
    }

    private fun searchWeb() {
        val url = "https://www.google.com/search?q=" + Uri.encode("drink recipes how to make")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No browser found to open the web page", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    private fun MoreScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "More Drink Info", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = AppColors.Green)
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Tap the button below to search for drink recipes in your phone browser.",
                fontSize = 17.sp,
                color = AppColors.Green
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { searchWeb() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Search Drinks in Browser")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Back")
            }
        }
    }
}
