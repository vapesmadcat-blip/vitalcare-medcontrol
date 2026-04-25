package com.vitalcare.medcontrol

import android.app.*
import android.content.*
import android.os.Build

object AlarmScheduler {

    fun schedule(context: Context, id: Int, time: Long, med: String, dose: String) {

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("med", med)
        intent.putExtra("dose", dose)

        val pending = PendingIntent.getBroadcast(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, time, pending)
        }
    }
}
