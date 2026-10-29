package com.example.island

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.io.File
import kotlin.system.exitProcess

class MainActivity : Activity() {
    private lateinit var errView: TextView
    private lateinit var crashFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        crashFile = File(filesDir, "crash.txt")
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            try { crashFile.writeText(Log.getStackTraceString(e)) } catch (_: Throwable) {}
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
        super.onCreate(savedInstanceState)

        try {
            if (Build.VERSION.SDK_INT >= 33) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        } catch (_: Throwable) {}

        val dp = resources.displayMetrics.density
        val pad = (24 * dp).toInt()

        fun btn(label: String, onClick: () -> Unit) = Button(this).apply {
            text = label
            setOnClickListener { onClick() }
        }

        errView = TextView(this).apply {
            textSize = 11f
            setTextIsSelectable(true)
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad * 2, pad, pad)
            addView(TextView(this@MainActivity).apply {
                text = "Dynamic Island v2"
                textSize = 26f
            })
            addView(btn("1) Üstte gösterme iznini ver") {
                startActivity(
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                )
            })
            addView(btn("2) Adayı başlat") { startIsland() })
            addView(btn("Adayı kapat") { stopService(Intent(this@MainActivity, IslandService::class.java)) })
            addView(btn("Hatayı temizle") { crashFile.delete(); showError() })
            addView(errView)
        }
        setContentView(ScrollView(this).apply { addView(root) })
    }

    override fun onResume() {
        super.onResume()
        showError()
    }

    private fun showError() {
        errView.text = if (crashFile.exists()) {
            "HATA (ekran görüntüsü at):\n\n" + try { crashFile.readText().take(2500) } catch (_: Throwable) { "okunamadi" }
        } else ""
    }

    private fun startIsland() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Önce 1. butonla izni ver", Toast.LENGTH_LONG).show()
            return
        }
        crashFile.delete()
        showError()
        try {
            startForegroundService(Intent(this, IslandService::class.java))
        } catch (t: Throwable) {
            try { crashFile.writeText(Log.getStackTraceString(t)) } catch (_: Throwable) {}
            showError()
        }
    }
}
