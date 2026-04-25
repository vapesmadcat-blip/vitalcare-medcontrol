package com.vitalcare.medcontrol;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(MedicationAlarmPlugin.class);
    super.onCreate(savedInstanceState);
  }
}
