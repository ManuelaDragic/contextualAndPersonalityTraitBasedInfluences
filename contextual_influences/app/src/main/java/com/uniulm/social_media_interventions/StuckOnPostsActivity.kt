package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first_stuck_on_posts.*
import org.json.JSONObject

class StuckOnPostsActivity : AppCompatActivity() {

    val question = "Have you ever been ”stuck” on unnecessary post for a long time?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_stuck_on_posts)

        this.button_submit.setOnClickListener {

            if ((!radio1.isChecked) && (!radio2.isChecked) && (!radio3.isChecked) && (!radio4.isChecked) && (!radio5.isChecked)) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()
            } else {

                val checkedRadioButtonId = radGroup.checkedRadioButtonId
                val radio = findViewById<RadioButton>(checkedRadioButtonId)
                val theAnswers = "${radio.text}"

                // Fill the JSON with the answer and continue to the next activity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("unnecessaryStuck", theAnswers)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, UsageAccessPermissionActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, AttentionCheckQuest::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


    }
    override fun onBackPressed() {
        val intent = Intent(this, AttentionCheckQuest::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}