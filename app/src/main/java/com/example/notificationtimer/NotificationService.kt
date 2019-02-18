package com.example.notificationtimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log

class NotificationService : Service() {

    private var textContent = ""
    private var notificationId = 1
    private var timeOutMinutes: Long = 10000
    private var interval: Long = 1000
    lateinit var pendingIntent: PendingIntent
    private lateinit var mBuilder: NotificationCompat.Builder
    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        object : CountDownTimer(timeOutMinutes, interval) {

            override fun onTick(millisUntilFinished: Long) {
                val channelId = getString(R.string.app_name)
                val mBuilder = NotificationCompat.Builder(this@NotificationService, channelId)
                textContent = "Time left: " + millisUntilFinished / (1000 * 60) + ":" + (millisUntilFinished / 1000) %
                        60
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel = NotificationChannel(channelId,"notification_timer", importance)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(true)
                    notificationChannel.description = getString(R.string.app_name)
                    notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    mBuilder.setChannelId(channelId)
                    notificationManager.createNotificationChannel(notificationChannel)
                }


                mBuilder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.message))
                    .setContentText(textContent)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                SingletonServiceManager.isMyServiceRunning = true
                startForeground(notificationId, mBuilder.build())

            }

            override fun onFinish() {
                SingletonServiceManager.isMyServiceRunning = false
                stopForeground(true)
                stopSelf()
            }
        }.start()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("service_stop","here")
        SingletonServiceManager.isMyServiceRunning = false
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }



}