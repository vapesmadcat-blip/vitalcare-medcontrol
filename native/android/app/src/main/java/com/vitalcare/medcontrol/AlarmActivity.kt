package com.vitalcare.medcontrol

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*

class AlarmActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        val med = intent.getStringExtra("med") ?: "Medicamento"
        val dose = intent.getStringExtra("dose") ?: ""

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val title = TextView(this)
        title.text = "💊 Hora da dose"
        title.textSize = 24f

        val msg = TextView(this)
        msg.text = "$med $dose"

        val btn = Button(this)
        btn.text = "OK"
        btn.setOnClickListener { finish() }

        layout.addView(title)
        layout.addView(msg)
        layout.addView(btn)

        setContentView(layout)
    }
}
