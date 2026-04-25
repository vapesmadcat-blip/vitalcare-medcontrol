package com.vitalcare.medcontrol

import android.app.*
import android.content.*
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val med = intent.getStringExtra("med") ?: "Medicamento"
        val dose = intent.getStringExtra("dose") ?: ""

        val channelId = "vitalcare_alarm"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Alarmes",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val fullIntent = Intent(context, AlarmActivity::class.java)
        fullIntent.putExtra("med", med)
        fullIntent.putExtra("dose", dose)

        val pending = PendingIntent.getActivity(
            context, 0, fullIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(context, channelId)
            .setContentTitle("💊 Hora da dose")
            .setContentText("$med $dose")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setFullScreenIntent(pending, true)
            .build()

        manager.notify(1, notif)
        context.startActivity(fullIntent)
    }
}
