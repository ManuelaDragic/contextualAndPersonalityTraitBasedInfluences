package com.uniulm.social_media_interventions

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log.i
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_ov_permission.*
import kotlinx.android.synthetic.main.activity_ov_permission.button_submit
import kotlinx.android.synthetic.main.activity_ua_permission.*

class OverlayPermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ov_permission)

        this.button_submit.setOnClickListener {

            val i = Intent()
            i.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
            i.data = Uri.parse("package:$packageName")
            startActivity(i)
            val intent = Intent(this, AdditionalPermissionActivity::class.java)
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
            }, 3000)


        }
    }

    override fun onBackPressed() {
        /*val intent = Intent(this, UsageAccessPermissionActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()*/
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (Settings.canDrawOverlays(this)){
                intent = Intent(this, AdditionalPermissionActivity::class.java)
                startActivity(intent)
                finish()
            }else{


            }
        }else{
            Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (Settings.canDrawOverlays(this)){
                intent = Intent(this, AdditionalPermissionActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                this.button_submit.setOnClickListener {
                    var intent = Intent(this, MainActivity::class.java)
                    val context = this
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                        if (Settings.canDrawOverlays(this)){
                            intent = Intent(this, AdditionalPermissionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            /*intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                            startActivity(intent)*/
                            settingActivityOpen()

                            //finish()
                        }
                    }else{
                        Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
        }


    }


    private fun settingActivityOpen() {
        val i = Intent()
        i.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        i.data = Uri.parse("package:$packageName")
        //val i = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(i)
        val btn = findViewById<Button>(R.id.button_submit)
        btn.text="Continue"
    }
}