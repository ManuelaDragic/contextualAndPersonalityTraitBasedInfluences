package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.uniulm.social_media_interventions.*
import kotlinx.android.synthetic.main.activity_after_rshci.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.button_back
import kotlinx.android.synthetic.main.activity_sess_feeling_after.radGroup
import kotlinx.android.synthetic.main.activity_start.*
import org.json.JSONArray

class rhsci_activity : AppCompatActivity() {

    val question = "How do you feel after the session?"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_rshci)
        Log.e("I AM IN rhsci","BEFORE BUTTON")
      /*  val delayTimeInSeconds = intent.getLongExtra("delayTimeInSeconds", 0)
        val delayTimeFormatted = intent.getLongExtra("delayTimeFormatted", 0)
        Log.e("VARIABLE ARRIVED in 0", delayTimeInSeconds.toString())*/
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        var pid = sharedPref.getString("App_Name", "App_Name")
        var intv = sharedPref.getString("Intv_Name", "Name of Intervention, lol")

        var view = findViewById<TextView>(R.id.tvQuestion)
        view.text = pid.toString()+" has been closed"

        var sub = findViewById<TextView>(R.id.tvSubtext)
        sub.text = "Please continue and answer some questions about this incident!"

        val delayTimeInSeconds = intent.getLongExtra("delayTimeinSeconds", 0L)

        Log.e("I AM IN rhsci",delayTimeInSeconds.toString())
        this.button_continue.setOnClickListener {
            val intent = Intent(this, rhsci1_activity::class.java)
            intent.putExtra("delayTimeinSeconds", delayTimeInSeconds)

            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        }

        /*this.button_back.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }*/

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    fun gon(){
        val intent = Intent(this, rhsci2_activity::class.java)
        Toast.makeText(
            this,
            "Thank you!", Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    fun saveres(answer1:String){
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("RSHCI1", answer1)
        editor.apply()
    }
}


