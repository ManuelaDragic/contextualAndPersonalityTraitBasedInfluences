package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.hardware.usb.UsbDevice.getDeviceName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_end_note.*
import kotlinx.android.synthetic.main.activity_first_social_media.button_back
import kotlinx.android.synthetic.main.activity_first_social_media.button_continue
import kotlinx.android.synthetic.main.activity_first_social_media.*
import kotlinx.android.synthetic.main.activity_tos.*

import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

class ToS_activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos)

        bb1.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        bb2.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }




    }

    override fun onBackPressed() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}