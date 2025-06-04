package com.uniulm.social_media_interventions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_ov_permission.*
import kotlinx.android.synthetic.main.activity_ua_permission.*
import kotlinx.android.synthetic.main.activity_ua_permission.button_submit


class UsageAccessPermissionActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ua_permission)

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
        val intent = Intent(this, BatteryPermissionActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (getGrantStatus()){
                intent = Intent(this, OverlayPermissionActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                this.button_submit.setOnClickListener {
                    var intent = Intent(this, MainActivity::class.java)
                    val context = this
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                        if (getGrantStatus()){
                            intent = Intent(this, OverlayPermissionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            settingActivityOpen()
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

                    //val builder: AlertDialog.Builder = AlertDialog.Builder(context)

                    /*Handler().postDelayed({
                        builder.setMessage("If Main Service starts, you can continue, otherwise check your permissions.").setPositiveButton("Continue", dialogClickListener2)
                            .setCancelable(false)
                            .show()
                    }, 0)*/
                }
            }
        }else{
            Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
        }


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

    private fun settingActivityOpen() {
        val i = Intent()
        i.action = Settings.ACTION_USAGE_ACCESS_SETTINGS
        i.data = Uri.parse("package:$packageName")
        //val i = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(i)
        val btn = findViewById<Button>(R.id.button_submit)
        btn.text="Continue"
    }




}