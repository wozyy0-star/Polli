package com.example.island

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Island() {
    var expanded by remember { mutableStateOf(false) }
    var running by remember { mutableStateOf(true) }
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(running) {
        while (running) { delay(1000); seconds++ }
    }

    val anim = spring<androidx.compose.ui.unit.Dp>(
        dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow
    )
    val w by animateDpAsState(if (expanded) 320.dp else 130.dp, anim, label = "w")
    val h by animateDpAsState(if (expanded) 150.dp else 36.dp, anim, label = "h")
    val r by animateDpAsState(if (expanded) 36.dp else 18.dp, anim, label = "r")

    val time = "%02d:%02d".format(seconds / 60, seconds % 60)

    Box(
        Modifier
            .size(w, h)
            .clip(RoundedCornerShape(r))
            .background(Color.Black)
            .clickable { expanded = !expanded },
        contentAlignment = Alignment.Center
    ) {
        if (expanded) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(time, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { running = !running },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) { Text(if (running) "Durdur" else "Başlat") }
                    Button(
                        onClick = { seconds = 0 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
                    ) { Text("Sıfırla") }
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(8.dp).clip(CircleShape)
                        .background(if (running) Color(0xFF4CAF50) else Color.Gray)
                )
                Spacer(Modifier.width(8.dp))
                Text(time, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
