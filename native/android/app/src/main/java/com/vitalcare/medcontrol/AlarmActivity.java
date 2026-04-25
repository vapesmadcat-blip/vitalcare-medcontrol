package com.vitalcare.medcontrol;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;

public class AlarmActivity extends Activity {
  private MediaPlayer player;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Window window = getWindow();
    window.addFlags(
      WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
      WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
      WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
      WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
    );

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
      setShowWhenLocked(true);
      setTurnScreenOn(true);
    }

    String title = getIntent().getStringExtra("title");
    String body = getIntent().getStringExtra("body");
    String med = getIntent().getStringExtra("med");
    String dose = getIntent().getStringExtra("dose");

    if (title == null || title.length() == 0) title = "💊 Hora do medicamento";
    if (body == null || body.length() == 0) body = "Está na hora da dose.";

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setGravity(Gravity.CENTER);
    layout.setPadding(42, 42, 42, 42);
    layout.setBackgroundColor(Color.rgb(7, 17, 31));

    TextView titleView = new TextView(this);
    titleView.setText(title);
    titleView.setTextColor(Color.WHITE);
    titleView.setTextSize(30);
    titleView.setTypeface(Typeface.DEFAULT_BOLD);
    titleView.setGravity(Gravity.CENTER);

    TextView bodyView = new TextView(this);
    String detail = body + "\n\n" + (med == null ? "" : med) + (dose == null || dose.length() == 0 ? "" : " · " + dose);
    bodyView.setText(detail);
    bodyView.setTextColor(Color.rgb(231,237,247));
    bodyView.setTextSize(22);
    bodyView.setGravity(Gravity.CENTER);
    bodyView.setPadding(0, 28, 0, 28);

    Button close = new Button(this);
    close.setText("✅ OK, estou ciente");
    close.setTextSize(20);
    close.setOnClickListener(v -> {
      stopSound();
      NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      if (nm != null) nm.cancelAll();
      finish();
    });

    layout.addView(titleView);
    layout.addView(bodyView);
    layout.addView(close);
    setContentView(layout);

    playSound();
  }

  private void playSound() {
    try {
      Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
      if (uri == null) uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      player = MediaPlayer.create(this, uri);
      if (player != null) {
        player.setLooping(true);
        player.start();
      }
    } catch (Exception ignored) {}
  }

  private void stopSound() {
    try {
      if (player != null) {
        player.stop();
        player.release();
        player = null;
      }
    } catch (Exception ignored) {}
  }

  @Override
  protected void onDestroy() {
    stopSound();
    super.onDestroy();
  }
}
