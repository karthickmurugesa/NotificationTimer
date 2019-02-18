package com.example.notificationtimer

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isMyServiceRunning()) {
            val serviceIntent = Intent(this, NotificationService::class.java)
            ContextCompat.startForegroundService(this,serviceIntent)
        }
    }

    private fun isMyServiceRunning(): Boolean {
        return SingletonServiceManager.isMyServiceRunning
    }

}
