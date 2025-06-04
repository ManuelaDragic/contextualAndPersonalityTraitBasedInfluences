package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_gender.*
import kotlinx.android.synthetic.main.activity_first_gender.button_back
import kotlinx.android.synthetic.main.activity_first_gender.button_continue
import org.json.JSONObject

class GenderActivity : AppCompatActivity() {

    val question = "What's your gender?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_gender)

        this.button_continue.setOnClickListener {

            if ((!rb1.isChecked()) && (!rb2.isChecked()) && (!rb3.isChecked()) && (!rb4.isChecked()) && (!rb5.isChecked())) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()
            } else {

                val checkedRadioButtonId = radGroup.checkedRadioButtonId
                Log.e("rbid", checkedRadioButtonId.toString())
                val radio = findViewById<RadioButton>(checkedRadioButtonId)
                val theAnswers = "${radio.text}"

                // Fill the JSON with the answer and continue to the next activity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("gender", theAnswers)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()


                val intent = Intent(this, SocialMediaActivity::class.java)
                intent.putExtra("radioChoice", checkedRadioButtonId)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, AgeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, AgeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
