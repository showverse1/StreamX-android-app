package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.FirestoreService
import com.example.ui.MainNavigation
import com.example.ui.StreamXViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: StreamXViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHighRefreshRate()
        FirestoreService.initialize(this)
        com.example.data.FirebaseAuthService.initialize(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBg
                ) {
                    MainNavigation(viewModel = viewModel)
                }
            }
        }
    }

    private fun enableHighRefreshRate() {
        try {
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val currentDisplay = display
                val modes = currentDisplay?.supportedModes
                val highRateMode = modes?.maxByOrNull { it.refreshRate }
                val targetRefreshRate = highRateMode?.refreshRate ?: 120f
                val params = window.attributes
                if (highRateMode != null) {
                    params.preferredDisplayModeId = highRateMode.modeId
                }
                params.preferredRefreshRate = targetRefreshRate
                window.attributes = params
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                val modes = window.windowManager.defaultDisplay?.supportedModes
                val highRateMode = modes?.maxByOrNull { it.refreshRate }
                if (highRateMode != null && highRateMode.refreshRate >= 90f) {
                    val params = window.attributes
                    params.preferredDisplayModeId = highRateMode.modeId
                    params.preferredRefreshRate = highRateMode.refreshRate
                    window.attributes = params
                }
            }
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "High refresh rate setup handled: ${e.message}")
        }
    }
}
