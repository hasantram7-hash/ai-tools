package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.CollectionOfRamApp
import com.example.ui.theme.MyApplicationTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    // Initialize Google Mobile Ads SDK in background thread
    MobileAds.initialize(this) {}

    setContent {
      MyApplicationTheme {
        CollectionOfRamApp()
      }
    }
  }
}

