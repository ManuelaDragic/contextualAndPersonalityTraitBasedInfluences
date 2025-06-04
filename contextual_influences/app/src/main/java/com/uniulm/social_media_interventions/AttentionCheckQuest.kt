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

class AttentionCheckQuest : AppCompatActivity() {

    val question = "It is important that you pay attention to this study. Please tick strongly agree"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attention_checkquest)

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

                // Add to the JSON if the attention check was successful
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                if(theAnswers=="strongly agree"){
                    questionnaireJSON.put("attentioncheck", true)
                }else{
                    questionnaireJSON.put("attentioncheck", false)
                }

                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, StuckOnPostsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, ReachingEndActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, ReachingEndActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}