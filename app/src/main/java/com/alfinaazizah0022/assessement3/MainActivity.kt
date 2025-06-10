package com.alfinaazizah0022.assessement3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alfinaazizah0022.assessement3.screen.MainScreen
import com.alfinaazizah0022.assessement3.ui.theme.Assessement3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assessement3Theme {
                MainScreen()
            }
        }
    }
}