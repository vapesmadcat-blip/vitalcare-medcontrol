package com.vitalcare.medcontrol;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
  private static final String CHANNEL_ID = "medcontrol_alarm";

  @Override
  public void onReceive(Context context, Intent intent) {
    int id = intent.getIntExtra("id", (int) (System.currentTimeMillis() % 100000));
    String title = intent.getStringExtra("title");
    String body = intent.getStringExtra("body");
    String med = intent.getStringExtra("med");
    String dose = intent.getStringExtra("dose");

    if (title == null || title.length() == 0) title = "💊 Hora do medicamento";
    if (body == null || body.length() == 0) body = "Está na hora da dose.";

    wakeScreen(context);
    vibrate(context);
    createChannel(context);

    Intent fullScreenIntent = new Intent(context, AlarmActivity.class);
    fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    fullScreenIntent.putExtra("title", title);
    fullScreenIntent.putExtra("body", body);
    fullScreenIntent.putExtra("med", med == null ? "" : med);
    fullScreenIntent.putExtra("dose", dose == null ? "" : dose);

    PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
      context,
      id,
      fullScreenIntent,
      PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle(title)
      .setContentText(body)
      .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setCategory(NotificationCompat.CATEGORY_ALARM)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setAutoCancel(false)
      .setOngoing(true)
      .setDefaults(NotificationCompat.DEFAULT_ALL)
      .setFullScreenIntent(fullScreenPendingIntent, true)
      .setContentIntent(fullScreenPendingIntent);

    try {
      NotificationManagerCompat.from(context).notify(id, builder.build());
      context.startActivity(fullScreenIntent);
    } catch (SecurityException e) {
      // Permissão de notificação negada no Android 13+.
    } catch (Exception e) {
      // Alguns fabricantes bloqueiam startActivity em background; fullScreen notification ainda tenta exibir.
    }
  }

  private void createChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (manager.getNotificationChannel(CHANNEL_ID) != null) return;

      Uri soundUri = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI;
      AudioAttributes attrs = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build();

      NotificationChannel channel = new NotificationChannel(
        CHANNEL_ID,
        "VitalCare Alarmes de Medicação",
        NotificationManager.IMPORTANCE_HIGH
      );
      channel.setDescription("Alarmes em tela cheia para doses de medicamentos.");
      channel.enableLights(true);
      channel.setLightColor(Color.CYAN);
      channel.enableVibration(true);
      channel.setVibrationPattern(new long[]{0, 700, 300, 700, 300, 1000});
      channel.setSound(soundUri, attrs);
      channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
      manager.createNotificationChannel(channel);
    }
  }

  private void wakeScreen(Context context) {
    try {
      PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      PowerManager.WakeLock wl = pm.newWakeLock(
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
        "VitalCare:MedicationAlarm"
      );
      wl.acquire(12000);
    } catch (Exception ignored) {}
  }

  private void vibrate(Context context) {
    try {
      Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
      if (v == null) return;
      long[] pattern = new long[]{0, 700, 300, 700, 300, 1000};
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createWaveform(pattern, -1));
      } else {
        v.vibrate(pattern, -1);
      }
    } catch (Exception ignored) {}
  }
}
