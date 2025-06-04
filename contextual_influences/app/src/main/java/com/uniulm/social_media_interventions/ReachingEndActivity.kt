package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_reaching_end.*
import kotlinx.android.synthetic.main.activity_first_reaching_end.button_back
import kotlinx.android.synthetic.main.activity_first_reaching_end.button_continue
import kotlinx.android.synthetic.main.activity_first_reaching_end.radGroup
import org.json.JSONObject

class ReachingEndActivity : AppCompatActivity() {

    val question = "Have you ever felt that you ”scrolled” without reaching the end?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_reaching_end)

        this.button_continue.setOnClickListener {

            if ((!rad1.isChecked()) && (!rad2.isChecked()) && (!rad3.isChecked()) && (!rad4.isChecked()) && (!rad5.isChecked())) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()
            } else {

                val checkedRadioButtonId = radGroup.checkedRadioButtonId
                val radio = findViewById<RadioButton>(checkedRadioButtonId)
                val theAnswers = "${radio.text}"

                // Fill the JSON with the answer and continue to the next acitivity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("scrollEnd", theAnswers)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, AttentionCheckQuest::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, StuckSocialMediaActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, StuckSocialMediaActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}