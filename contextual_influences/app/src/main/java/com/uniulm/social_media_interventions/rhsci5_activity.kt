package com.uniulm.social_media_interventions

import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.APP_OPS_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.*
import com.uniulm.social_media_interventions.*
import kotlinx.android.synthetic.main.activity_after_rshci.*
import kotlinx.android.synthetic.main.activity_after_rshci.button_continue
import kotlinx.android.synthetic.main.activity_after_rshci5.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.button_back
import kotlinx.android.synthetic.main.activity_sess_feeling_after.radGroup
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class rhsci5_activity : AppCompatActivity() {

    val question = "How do you feel after the session?"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_rshci5)

        this.button_continue.setOnClickListener {

            var answer5 = 42
            if(this.rad1.isChecked){
                answer5 = 5
                saveres(answer5)
                gon()
            }else if (rad2.isChecked){
                answer5 = 4
                saveres(answer5)
                gon()
            }else if (rad3.isChecked){
                answer5 = 3
                saveres(answer5)
                gon()
            }else if (rad4.isChecked){
                answer5 = 2
                saveres(answer5)
                gon()
            }else if (rad5.isChecked){
                answer5 = 1
                saveres(answer5)
                gon()
            }else{
                Toast.makeText(
                    this,
                    "Please make a choice in every row!", Toast.LENGTH_SHORT).show()
            }

            val sharedPref = getSharedPreferences("InfiniteScroll", 0)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt("RSHCI5", answer5)
            editor.apply()

            sendData()
            //Thread.sleep(2000)
            val intent = Intent(this, ThankActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


            /*if ((radGroup.checkedRadioButtonId == -1) || (radGroup2.checkedRadioButtonId == -1) || (radGroup3.checkedRadioButtonId == -1)) {
                Toast.makeText(
                    this,
                    "Please make a choice in every row!", Toast.LENGTH_SHORT
                ).show()

            } else {*/




                /*val appsArray = JSONArray()

                val checkedRadioButtonId1 = radGroup.checkedRadioButtonId
                val checkedRadioButtonId2 = radGroup2.checkedRadioButtonId
                val checkedRadioButtonId3 = radGroup3.checkedRadioButtonId
                val radio1 = findViewById<RadioButton>(checkedRadioButtonId1)
                appsArray.put(radio1.text)
                val radio2 = findViewById<RadioButton>(checkedRadioButtonId2)
                appsArray.put(radio2.text)
                val radio3 = findViewById<RadioButton>(checkedRadioButtonId3)
                appsArray.put(radio3.text)*/

                // Fill the JSON with the answer and continue to the next activity
                /*val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val sessionQuestionnaireAnswers =
                    sharedPref.getString("sessionQuestionnaireAnswers", "")
                val sessionQuestionnaireJSON = JSONObject(sessionQuestionnaireAnswers)
                    .put("feelingAboutSession", appsArray.toString())
                Log.e("JSON", sessionQuestionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("sessionQuestionnaireAnswers", sessionQuestionnaireJSON.toString())
                editor.apply()

                Toast.makeText(
                    this,
                    "Thank you!", Toast.LENGTH_SHORT
                ).show()

                val sessionID = sharedPref.getString("SESSION_ID", "")
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

                sessionQuestionnaireFinalJSON.put("sessionQuestionnaire", sessionQuestionnaireJSON)

                // As it is the last question we can send the final JSON to the server and let it create the session
                //val helper = Helper();
                helper.sendRequest(
                    context,
                    object : VolleyCallBack {
                        override fun onSuccess(response: JSONObject?) {
                        }

                        override fun onFailure(response: JSONObject?) {
                        }
                    },
                    "/user/session/${sessionID}", "PATCH", sessionQuestionnaireFinalJSON.toString()
                )


            }*/
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, rhsci4_activity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, rhsci4_activity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    fun sendData(){
        val url = "http://134.60.152.201:5000/write_intervention_study_csv"
        val username = "user"
        val password = "password"
        val credentials = Credentials.basic(username,password)
        var studyObj = JSONObject()

        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        var pid = sharedPref.getString("pID", "EMPTY")
        var app_name = sharedPref.getString("App_Name", "EMPTY")
        var intv_name = sharedPref.getString("Intv_Name", "EMPTY")
        var t1 = sharedPref.getString("t1", "EMPTY")
        var t2 = sharedPref.getString("t2", "EMPTY")
        //var delta = sharedPref.getString("Delta", "EMPTY")
        var rshci1 = sharedPref.getInt("RSHCI1", 404)
        var rshci2 = sharedPref.getInt("RSHCI2", 404)
        var rshci3 = sharedPref.getInt("RSHCI3", 404)
        var rshci4 = sharedPref.getInt("RSHCI4", 404)
        var rshci5 = sharedPref.getInt("RSHCI5", 404)
        //TODO: Change situation
        var situation = "Toilet"

        Log.e("Shared Pref", sharedPref.all.toString())



        studyObj.put("pID", pid)
        studyObj.put("App_Name", app_name)
        studyObj.put("Intv_Name", intv_name)
        studyObj.put("t1", t1)
        studyObj.put("t2", t2)
        //studyObj.put("Delta", delta)
        studyObj.put("rshci1", rshci1)
        studyObj.put("rshci2", rshci2)
        studyObj.put("rshci3", rshci3)
        studyObj.put("rshci4", rshci4)
        studyObj.put("rshci5", rshci5)
        studyObj.put("situation", situation)
        //studyObj.put("pID", pid)

        Log.e("Study_JSON", studyObj.toString())

        postJsonToServer(url, studyObj, credentials){
                response ->
            Log.d("Response", response.toString())
        }

    }


    fun postJsonToServer(url: String, jsonObject: JSONObject, credentials: String, callback: (response: String?) -> Unit) {
        val client = OkHttpClient()
        //val intent = Intent(this, WelcomeActivity::class.java)
        var intent = Intent(this, AppCheckerService::class.java)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .addHeader("Authorization", credentials)
            .addHeader("Content-Type", "text/csv")
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }
        })
    }





    fun gon(){
        /*val intent = Intent(this, MainActivity::class.java)
        /*Toast.makeText(
            this,
            "Thank you!", Toast.LENGTH_SHORT
        ).show()*/
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
        finish()
    }

    fun saveres(answer1:Int){
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt("RSHCI5", answer1)
        editor.apply()
    }

    private fun getGrantStatus(): Boolean {
        val appOps = applicationContext
            .getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), applicationContext.packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            (applicationContext.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED) &&
                    (applicationContext.checkCallingOrSelfPermission((Manifest.permission.SYSTEM_ALERT_WINDOW)) == PackageManager.PERMISSION_GRANTED)

        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    private fun isAppCheckerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.uniulm.social_media_interventions.AppCheckerService" == service.service.className) {
                return true
            }
        }
        return false
    }
}