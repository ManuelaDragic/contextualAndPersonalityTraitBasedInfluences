package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_age.button_continue
import org.json.JSONObject

class AgeActivity : AppCompatActivity() {

    val question = "How old are you?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_age)

        val agePicker = findViewById<NumberPicker>(R.id.agePicker)
        agePicker.minValue = 18
        agePicker.maxValue = 99

        this.button_continue.setOnClickListener {

            agePicker.setOnValueChangedListener(onValueChangeListener)
            val choice = "${agePicker.value}"

                // Fill the JSON with the answer and continue to the next acitivity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireJSON = JSONObject()
                    .put("age", choice)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, GenderActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        }
    }
var onValueChangeListener =
    NumberPicker.OnValueChangeListener { numberPicker, i, i1 ->
        Toast.makeText(
            this,
            "selected number " + numberPicker.value, Toast.LENGTH_SHORT
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

}