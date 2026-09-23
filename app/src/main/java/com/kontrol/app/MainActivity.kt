package com.kontrol.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kontrol.app.ui.KontrolApp
import com.kontrol.app.ui.theme.KontrolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KontrolTheme {
                KontrolApp(applicationContext)
            }
        }
    }
}
