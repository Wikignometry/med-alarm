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
import androidx.core.app.NotificationCompat
import com.medalarm.medalarm.util.logDebug
import com.medalarm.medalarm.util.scheduleAlarm
import java.lang.Exception
import java.util.*

private const val NOTIF_ID = 1
private const val CHANNEL_ID = "ALARM"

/*
 * Receiver owned by TriggeredAlarmService that handles the dismiss and snooze buttons
 * Handles dismissing the alarm and snoozing (adding an additional alarm)
 */
class TriggeredAlarmServiceReceiver(private val service: TriggeredAlarmService) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1 != null && p0 != null) {

            val isSnooze = p1.getBooleanExtra("snooze", false)
            val notifId = p1.getIntExtra("notif_id", -10)
            logDebug(isSnooze.toString())

            // If this is a snooze, set a new alarm in the future
            if (isSnooze) {
                val alarmManager = p0.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                // Snoozing is hard-coded 5 minutes currently
                val cal = Calendar.getInstance()
                cal.add(Calendar.MINUTE, 5)

                logDebug("scheduled snoozed alarm")
                scheduleAlarm(p0, alarmManager, cal.timeInMillis, notifId)
            }
        }

        service.stopCommand()

        logDebug("cancelled notif")
    }
}

/*
 * Service that handles the persistent notification and alarm sound playback while an alarm is currently triggered
 */
class TriggeredAlarmService : Service() {

    private val player = MediaPlayer()
    private var notif = Notification()

    private val receiver = TriggeredAlarmServiceReceiver(this)

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        initNotification()

        val filter = IntentFilter("com.medalarm.onalarmend")
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
    }

    fun stopCommand() {
        player.stop()
        stopForeground(true)
        stopSelf()
    }

    private fun initNotification() {
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
        alarmIntent.putExtra("start_date", Date().time)

        val immutableFlag =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        val alarmPendingIntent = PendingIntent.getActivity(
            this,
            0,
            alarmIntent,
            immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmSnoozePendingIntent = PendingIntent.getBroadcast(this, -2, getSnoozeIntent(), immutableFlag)
        val alarmDismissPendingIntent = PendingIntent.getBroadcast(this, -1, getDismissIntent(), immutableFlag)

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
            .addAction(R.drawable.ic_snooze, "Snooze", alarmSnoozePendingIntent)
            .addAction(R.drawable.ic_alarm_off, "Dismiss", alarmDismissPendingIntent)
            .build()

    }

    private fun initMediaPlayer() {
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
                logDebug("Error occurred while playing audio.")
                true
            }
        }

        player.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())

        player.setDataSource(this, alarmSoundUri)
    }
}

fun getDismissIntent(): Intent {
    val alarmDismissIntent = Intent("com.medalarm.onalarmend")
    alarmDismissIntent.putExtra("notif_id", NOTIF_ID)

    return alarmDismissIntent
}

fun getSnoozeIntent(): Intent {
    val alarmSnoozeIntent = Intent("com.medalarm.onalarmend")
    alarmSnoozeIntent.putExtra("notif_id", NOTIF_ID)
    alarmSnoozeIntent.putExtra("snooze", true)

    return alarmSnoozeIntent
}