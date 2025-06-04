package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View.VISIBLE
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_first_usage_reasons.*
import org.json.JSONArray
import org.json.JSONObject


class UsageReasonsActivity : AppCompatActivity() {

    val question = "What are the main reasons for your social media usage?"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_usage_reasons)

        // The following is used to allow own creating of reasons
        // It will make it possible to add new fields to fill in these reasons
        val linLayReasons = findViewById<LinearLayout>(R.id.linLayReasons)
        val linLayReason = LinearLayout(this)
        linLayReason.orientation = LinearLayout.HORIZONTAL
        linLayReason.id = 500 + (linLayReasons.childCount)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        params.setMargins(0, 8, 0, 0)

        linLayReason.layoutParams = params

        val editTextReason = EditText(this)
        editTextReason.setBackgroundResource(R.drawable.edit_text_background)
        editTextReason.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.5f
        )
        editTextReason.hint = "Reason"

        val deleteBtn = MaterialButton(
            ContextThemeWrapper(
                this,
                R.style.Widget_MaterialComponents_Button_OutlinedButton
            )
        )
        deleteBtn.id = 400 + (linLayReasons.childCount)
        val btnParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.3f
        )
        btnParams.setMargins(8, 0, 0, 0)
        deleteBtn.layoutParams = btnParams
        deleteBtn.setBackgroundColor(getColor(R.color.white))
        deleteBtn.visibility = VISIBLE
        deleteBtn.setIconResource(R.drawable.ic_baseline_delete_24)
        deleteBtn.setIconTintResource(R.color.black)

        deleteBtn.setOnClickListener {
            val linLayReasonId = deleteBtn.id + 100

            val foundLinLayReason = findViewById<LinearLayout>(linLayReasonId)
            val index = linLayReasons.indexOfChild(foundLinLayReason)
            linLayReasons.removeViewAt(index)
        }

        linLayReason.addView(editTextReason)
        linLayReason.addView(deleteBtn)

        linLayReasons.addView(linLayReason, linLayReasons.childCount)

        addReasonBtn.setOnClickListener {
            val linLayReason = LinearLayout(this)
            linLayReason.id = 500 + (linLayReasons.childCount)
            linLayReason.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )

            params.setMargins(0, 8, 0, 0)

            linLayReason.layoutParams = params

            val editTextReason = EditText(this)
            editTextReason.setBackgroundResource(R.drawable.edit_text_background)
            editTextReason.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.5f
            )
            editTextReason.hint = "Reason"

            val deleteBtn = MaterialButton(
                ContextThemeWrapper(
                    this,
                    R.style.Widget_MaterialComponents_Button_OutlinedButton
                )
            )
            val btnParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.3f
            )
            btnParams.setMargins(8, 0, 0, 0)
            deleteBtn.id = 400 + (linLayReasons.childCount)
            deleteBtn.layoutParams = btnParams
            deleteBtn.setBackgroundColor(getColor(R.color.white))
            deleteBtn.setIconResource(R.drawable.ic_baseline_delete_24)
            deleteBtn.setIconTintResource(R.color.black)

            deleteBtn.setOnClickListener {
                val linLayReasonId = deleteBtn.id + 100
                val foundLinLayReason = findViewById<LinearLayout>(linLayReasonId)
                val index = linLayReasons.indexOfChild(foundLinLayReason)
                linLayReasons.removeViewAt(index)
            }

            linLayReason.addView(editTextReason)
            linLayReason.addView(deleteBtn)

            linLayReasons.addView(linLayReason, linLayReasons.childCount)
        }

        this.button_continue.setOnClickListener {
            var empty = true;
            for (i in 0 until linLayReasons.childCount) {
                val linLayReason: LinearLayout = linLayReasons.getChildAt(i) as LinearLayout
                val reasonEdit: EditText = linLayReason.getChildAt(0) as EditText
                if (reasonEdit.text.toString() != "") {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()
            } else {
                val reasonsArray = JSONArray()
                for (i in 0 until linLayReasons.childCount) {
                    val linLayReason: LinearLayout = linLayReasons.getChildAt(i) as LinearLayout
                    val reasonEdit: EditText = linLayReason.getChildAt(0) as EditText
                    reasonsArray.put("${reasonEdit.text}")
                }

                // Fill the JSON with the answer and continue to the next activity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("mainReason", reasonsArray)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, StuckSocialMediaActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }

        this.button_back.setOnClickListener {
            val intent = Intent(this, TimeSpendActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, TimeSpendActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}