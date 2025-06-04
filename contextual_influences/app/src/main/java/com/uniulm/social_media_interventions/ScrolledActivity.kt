package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import com.uniulm.social_media_interventions.SessionSurvey.ReasonStoppedActivity
import kotlinx.android.synthetic.main.activity_scrolled.*

class ScrolledActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolled)

        // Fill the JSON based on the answer and continue to the next activity
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        this.button_yes.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("scrollSession", "true")
            editor.apply()
            //val intent = Intent(this, ReasonStoppedActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        this.button_no.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("scrollSession", "false")
            editor.apply()
            //val intent = Intent(this, ReasonStoppedActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }
}