package com.uniulm.social_media_interventions

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_end_note.*
import org.json.JSONObject

class EndNoteActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_note)

        finishButton.setOnClickListener {
            val endAnswers = JSONObject()
            val incidentsEdit: EditText = incidentsEdit as EditText
            val feelingsEdit: EditText = feelingEdit as EditText
            endAnswers.put("incidents", "${incidentsEdit.text}")
            endAnswers.put("feedback", "${feelingsEdit.text}")
            endAnswers.put("device", getDeviceName())
            Log.e("JSON", endAnswers.toString())

            val helper = Helper();
            /*helper.sendRequest(
                this,
                object : VolleyCallBack {
                    override fun onSuccess(response: JSONObject?) {

                    }

                    override fun onFailure(errorResponse: JSONObject?) {

                    }
                },
                "/user/addFeedback",
                "POST",
                endAnswers.toString()
            )*/
            finish()
        }
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }
}
