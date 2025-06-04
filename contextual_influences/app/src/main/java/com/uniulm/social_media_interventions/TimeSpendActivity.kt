package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first_time_spend.button_back
import kotlinx.android.synthetic.main.activity_first_time_spend.button_continue
import org.json.JSONObject


class TimeSpendActivity : AppCompatActivity() {

    val question = "Approximately how much time do you spend on social media during a day?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_spend)

        val np = findViewById<NumberPicker>(R.id.timePickerM)
        val npH = findViewById<NumberPicker>(R.id.timePickerH)

        npH.minValue = 0
        npH.maxValue = 23

        np.minValue = 0
        np.maxValue = 5

        val formatter = NumberPicker.Formatter { value ->
            val temp = value * 10
            "" + temp
        }
        np.setFormatter(formatter)

        this.button_continue.setOnClickListener {

            np.setOnValueChangedListener(onValueChangeListener)
            npH.setOnValueChangedListener(onValueChangeListener)
            val choice = "${npH.value}" + ":" + "${np.value * 10}"

            if (np.value == 0 && npH.value == 0) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()
            } else {

                // Fill the JSON with the answer and continue to the next activity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("approximateTimeSpending", choice)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, UsageReasonsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, UserGroupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    var onValueChangeListener =
        NumberPicker.OnValueChangeListener { numberPicker, i, i1 ->
            Toast.makeText(
                this,
                "your selected time " + numberPicker.value, Toast.LENGTH_SHORT
            )
        }

    override fun onBackPressed() {
        val intent = Intent(this, UserGroupActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}