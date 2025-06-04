package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_social_media.button_back
import kotlinx.android.synthetic.main.activity_first_social_media.button_continue
import kotlinx.android.synthetic.main.activity_first_social_media.*

import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

class SocialMediaActivity : AppCompatActivity() {

    val question = "Which of these social media sites do you use on a dailybasis?"

    var othersHasInput = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_social_media)

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }

        val othersText = findViewById(R.id.otherEdit) as EditText
        othersText.afterTextChanged {
            if (othersText.text.toString().isEmpty()) {
                othersHasInput = false
                return@afterTextChanged
            } else {
                if (!Pattern.matches(
                        "^[A-Za-z ,0-9]+$",
                        othersText.text.toString()
                    )
                ) {
                    othersText.setError("Invalid characters!");
                } else {
                    othersHasInput = true
                }
            }
        }

        this.button_continue.setOnClickListener {
            if ((!checkBox0.isChecked()) && (!checkBox9.isChecked()) && (!checkBox7.isChecked()) && (!checkBox1.isChecked()) && (!checkBox2.isChecked()) && (!checkBox3.isChecked()) && (!checkBox4.isChecked()) && othersText.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please make a choice", Toast.LENGTH_SHORT
                ).show()

            } else {

                val appsArray = JSONArray()

                // Set the answers based on all clicked checkboxes
                // If adding new apps you have to append them here as well as the if clause
                // above to make sure at least one is checked
                val theAnswers = "" +
                        (if (checkBox0.isChecked) {
                            "${checkBox0.text}"
                            appsArray.put(checkBox0.text)
                        } else " ") +
                        (if (checkBox1.isChecked) {
                            "${checkBox1.text}"
                            appsArray.put(checkBox1.text)
                        } else " ") +
                        (if (checkBox2.isChecked) {
                            "${checkBox2.text}"
                            appsArray.put(checkBox2.text)
                        } else " ") +
                        (if (checkBox3.isChecked) {
                            "${checkBox3.text}"
                            appsArray.put(checkBox3.text)
                        } else " ") +
                        (if (checkBox4.isChecked) {
                            "${checkBox4.text}"
                            appsArray.put(checkBox4.text)
                        } else " ") +
                        (if (othersHasInput) {
                            appsArray.put("Others: ${othersText.text}")
                        } else " ") +
                        (if (checkBox7.isChecked) {
                            "${checkBox7.text}" + "${othersText.text}"
                            appsArray.put(checkBox7.text)
                        } else " ") +
                        (if (checkBox9.isChecked) {
                            "${checkBox9.text}" + "${othersText.text}"
                            appsArray.put(checkBox9.text)
                        } else " ")

                // Fill the JSON with the answer and continue to the next activity
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
                val questionnaireJSON = JSONObject(questionnaireAnswers)
                    .put("apps", appsArray)
                Log.e("JSON", questionnaireJSON.toString())
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("questionnaireAnswers", questionnaireJSON.toString())
                editor.apply()

                val intent = Intent(this, UserGroupActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
        this.button_back.setOnClickListener {
            val intent = Intent(this, GenderActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, GenderActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}