package com.uniulm.social_media_interventions

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.uniulm.social_media_interventions.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.*
import org.json.JSONArray

class FeelingAfterLockActivity : AppCompatActivity() {

    val question = "How do you feel after the session?"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_feeling_after)

        this.button_submit.setOnClickListener {

            if ((radGroup.checkedRadioButtonId == -1) || (radGroup2.checkedRadioButtonId == -1) || (radGroup3.checkedRadioButtonId == -1)) {
                Toast.makeText(
                    this,
                    "Please make a choice in every row!", Toast.LENGTH_SHORT
                ).show()

            } else {

                val appsArray = JSONArray()

                val checkedRadioButtonId1 = radGroup.checkedRadioButtonId
                val checkedRadioButtonId2 = radGroup2.checkedRadioButtonId
                val checkedRadioButtonId3 = radGroup3.checkedRadioButtonId
                val radio1 = findViewById<RadioButton>(checkedRadioButtonId1)
                appsArray.put(radio1.text)
                val radio2 = findViewById<RadioButton>(checkedRadioButtonId2)
                appsArray.put(radio2.text)
                val radio3 = findViewById<RadioButton>(checkedRadioButtonId3)
                appsArray.put(radio3.text)

                // Fill the JSON with the answer and continue to the next activity
                /*val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val sessionQuestionnaireAnswers =
                    sharedPref.getString("sessionQuestionnaireAnswers", "")
                val sessionQuestionnaireJSON = JSONObject(sessionQuestionnaireAnswers)
                    .put("feelingAboutSession", appsArray.toString())
                Log.e("JSON", sessionQuestionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("sessionQuestionnaireAnswers", sessionQuestionnaireJSON.toString())
                editor.apply()*/

                Toast.makeText(
                    this,
                    "Thank you!", Toast.LENGTH_SHORT
                ).show()

                /*val sessionID = sharedPref.getString("SESSION_ID", "")
                val activity = this;
                val context = this;

                val sessionQuestionnaireFinalJSON = JSONObject()
                val scrollSession = sharedPref.getString(
                    "scrollSession",
                    null
                )

                if (scrollSession != null) {
                    sessionQuestionnaireFinalJSON.put("scrollSession", scrollSession)
                }

                sessionQuestionnaireFinalJSON.put("sessionQuestionnaire", sessionQuestionnaireJSON)*/

                // As it is the last question we can send the final JSON to the server and let it create the session
                val helper = Helper();
                /*helper.sendRequest(
                    context,
                    object : VolleyCallBack {
                        override fun onSuccess(response: JSONObject?) {
                        }

                        override fun onFailure(response: JSONObject?) {
                        }
                    },
                    "/user/session/${sessionID}", "PATCH", sessionQuestionnaireFinalJSON.toString()
                )*/

                //TODO:Change Intent
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, rhsci1_activity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}