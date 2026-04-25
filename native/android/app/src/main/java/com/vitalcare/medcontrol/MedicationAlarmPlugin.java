package com.vitalcare.medcontrol;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

@CapacitorPlugin(
  name = "MedicationAlarm",
  permissions = {
    @Permission(strings = { Manifest.permission.POST_NOTIFICATIONS }, alias = "notifications")
  }
)
public class MedicationAlarmPlugin extends Plugin {

  @PluginMethod
  public void requestPermissions(PluginCall call) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requestPermissionForAlias("notifications", call, "permissionCallback");
      return;
    }

    JSObject ret = new JSObject();
    ret.put("notifications", "granted");
    call.resolve(ret);
  }

  @PluginMethod
  public void permissionCallback(PluginCall call) {
    JSObject ret = new JSObject();
    ret.put("notifications", "granted");
    call.resolve(ret);
  }

  @PluginMethod
  public void openExactAlarmSettings(PluginCall call) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        getActivity().startActivity(intent);
      }
      call.resolve();
    } catch (Exception e) {
      call.reject("Não foi possível abrir a configuração de alarme exato", e);
    }
  }

  @PluginMethod
  public void schedule(PluginCall call) {
    Integer id = call.getInt("id");
    Long atMillis = call.getLong("atMillis");
    String title = call.getString("title", "💊 Hora do medicamento");
    String body = call.getString("body", "Está na hora da dose.");
    String med = call.getString("med", "");
    String dose = call.getString("dose", "");

    if (id == null || atMillis == null) {
      call.reject("Campos obrigatórios: id e atMillis");
      return;
    }

    try {
      Context context = getContext();
      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        JSObject ret = new JSObject();
        ret.put("scheduled", false);
        ret.put("needsExactAlarmPermission", true);
        call.resolve(ret);
        return;
      }

      Intent intent = new Intent(context, AlarmReceiver.class);
      intent.setAction("com.vitalcare.medcontrol.MEDICATION_ALARM");
      intent.putExtra("id", id);
      intent.putExtra("title", title);
      intent.putExtra("body", body);
      intent.putExtra("med", med);
      intent.putExtra("dose", dose);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );

      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, atMillis, pendingIntent);

      JSObject ret = new JSObject();
      ret.put("scheduled", true);
      call.resolve(ret);
    } catch (Exception e) {
      call.reject("Falha ao agendar alarme nativo", e);
    }
  }

  @PluginMethod
  public void cancel(PluginCall call) {
    Integer id = call.getInt("id");
    if (id == null) {
      call.resolve();
      return;
    }

    try {
      Context context = getContext();
      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

      Intent intent = new Intent(context, AlarmReceiver.class);
      intent.setAction("com.vitalcare.medcontrol.MEDICATION_ALARM");

      PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );

      alarmManager.cancel(pendingIntent);
      pendingIntent.cancel();

      JSObject ret = new JSObject();
      ret.put("cancelled", true);
      call.resolve(ret);
    } catch (Exception e) {
      call.reject("Falha ao cancelar alarme", e);
    }
  }
}
