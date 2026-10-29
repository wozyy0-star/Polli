package com.example.island

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        setContent {
            MaterialTheme {
                Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Dynamic Island", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = {
                        startActivity(
                            Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName")
                            )
                        )
                    }) { Text("1) Üstte gösterme iznini ver") }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        if (Settings.canDrawOverlays(this@MainActivity)) {
                            startForegroundService(Intent(this@MainActivity, IslandService::class.java))
                        }
                    }) { Text("2) Adayı başlat") }
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = {
                        stopService(Intent(this@MainActivity, IslandService::class.java))
                    }) { Text("Adayı kapat") }
                }
            }
        }
    }
}
