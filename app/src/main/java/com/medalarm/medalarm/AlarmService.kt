package com.medalarm.medalarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.lang.Exception

class AlarmServiceReceiver(private val service: AlarmService) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        service.stopCommand()

        Log.d("medalarm","cancelled notif")
    }
}

class AlarmService : Service() {
    private val NOTIF_ID = 1
    private val CHANNEL_ID = "ALARM"

    private val player = MediaPlayer()
    private var notif = Notification()

    private val receiver = AlarmServiceReceiver(this)

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        initNotification()

        val filter = IntentFilter("com.medalarm.onalarmend");
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        player.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, notif)

        player.runCatching {
            isLooping = true
            prepare()
            start()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    fun stopCommand() {
        player.stop()
        stopForeground(true)
        stopSelf()
    }

    fun initNotification() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, "Alarm", importance)
            mChannel.description = "placeholder"
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(false)
            mChannel.setShowBadge(true)
            mChannel.setSound(null, null)
            notificationManager.createNotificationChannel(mChannel)
        }

        val alarmIntent = Intent(this, AlarmActivity::class.java)
        alarmIntent.action = "PILL_ALARM_ACTION"

        val alarmPendingIntent = PendingIntent.getActivity(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmDismissIntent = Intent("com.medalarm.onalarmend")
        alarmDismissIntent.putExtra("notif_id", NOTIF_ID)

        val alarmDismissPendingIntent = PendingIntent.getBroadcast(this, -1, alarmDismissIntent, PendingIntent.FLAG_IMMUTABLE)

        notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Pill Alarm!")
            .setContentText("It's time to take a pill!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(alarmPendingIntent)
            .setFullScreenIntent(alarmPendingIntent, true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .setSound(null)
            .addAction(R.drawable.ic_alarm_off, "Dismiss", alarmDismissPendingIntent)
            .build()

    }

    fun initMediaPlayer() {
        // temp
        var alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (alarmSoundUri == null) {
            // alert is null, using backup
            alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (alarmSoundUri == null) {
                // alert backup is null, using 2nd backup
                alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

                if (alarmSoundUri == null) {
                    throw Exception("alarmSoundUri is null after two fallbacks")
                }
            }
        }

        player.apply {
            setOnErrorListener { _, _, _ ->
                Log.d("medalarm", "Error occurred while playing audio.")
                true
            }
        }

        player.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())

        player.setDataSource(this, alarmSoundUri)
    }
}