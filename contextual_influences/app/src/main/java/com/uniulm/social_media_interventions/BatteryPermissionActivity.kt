package com.uniulm.social_media_interventions

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_bat_permission.*
import kotlinx.android.synthetic.main.activity_ov_permission.*
import kotlinx.android.synthetic.main.activity_ua_permission.*


class BatteryPermissionActivity : AppCompatActivity() {




    lateinit var powerManager:PowerManager





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        powerManager = applicationContext.getSystemService(POWER_SERVICE) as PowerManager



        /*this.button_submit.setOnClickListener {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            val intent = Intent(this, OverlayPermissionActivity::class.java)
            val context = this


            // Starts the settings activity allowing the user to give permissions to the usage stats
            val dialogClickListener2 =
                DialogInterface.OnClickListener { dialog2, which2 ->
                    when (which2) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                            startActivity(intent)
                            finish()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            Handler().postDelayed({
                builder.setMessage("Thanks.").setPositiveButton("Continue", dialogClickListener2)
                    .setCancelable(false)
                    .show()
            }, 0)
        }*/

    }

    override fun onBackPressed() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        //finish()
    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_bat_permission)


        val intent = Intent(this, UsageAccessPermissionActivity::class.java)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (getGrantStatus()){
                doOperation()
            }else{

                findViewById<Button>(R.id.button_submit).setOnClickListener {
                    if(getGrantStatus()){

                        startActivity(intent)
                        //finish()
                    }else{
                        findViewById<TextView>(R.id.tv1).text=""
                        findViewById<TextView>(R.id.tv2).text=""
                        findViewById<TextView>(R.id.tv3).text="Press the button to continue!"
                        settingActivityOpen()

                    }
                }
                /*this.button_submit.setOnClickListener {
                    var intent = Intent(this, MainActivity::class.java)
                    val context = this
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                        if (powerManager.isIgnoringBatteryOptimizations(packageName)){
                            intent = Intent(this, UsageAccessPermissionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            //intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                            //startActivity(intent)



                            //finish()
                        }
                    }else{
                        Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
                    }


                    //startActivity(intent)

                    // Starts the settings activity allowing the user to give permissions to the usage stats
                    val dialogClickListener2 =
                        DialogInterface.OnClickListener { dialog2, which2 ->
                            when (which2) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    overridePendingTransition(
                                        android.R.anim.fade_in,
                                        android.R.anim.fade_out
                                    )
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                                        if (Settings.canDrawOverlays(this)){
                                            intent = Intent(this, StartActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }else{
                                            intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                            startActivity(intent)

                                            //finish()
                                        }
                                    }else{
                                        Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }



                    /*Handler().postDelayed({
                        builder.setMessage("If Main Service starts, you can continue, otherwise check your permissions.").setPositiveButton("Continue", dialogClickListener2)
                            .setCancelable(false)
                            .show()
                    }, 0)*/
                }*/
            }
        }else{
            Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
        }




    }

    private fun getGrantStatus(): Boolean {
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }



    private fun settingActivityOpen() {
        val i = Intent()
        i.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        i.data = Uri.parse("package:$packageName")
        //val i = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(i)
        val btn = findViewById<Button>(R.id.button_submit)
        btn.text="Continue"
    }

    private fun doOperation() {
        intent = Intent(this, BatteryPermissionActivity::class.java)
        startActivity(intent)
        //finish()
    }

}