package com.uniulm.social_media_interventions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context.APP_OPS_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    lateinit var powerManager: PowerManager
    var checkboxclicked= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val studyended = sharedPref.getBoolean("studyended", true)
        if (studyended == true){
            setContentView(R.layout.activity_welcome)
            powerManager = applicationContext.getSystemService(POWER_SERVICE) as PowerManager

            val tos = this.findViewById<TextView>(R.id.tosview)

            tos.setOnClickListener {
                val intent = Intent(this, ToS_activity::class.java)
                startActivity(intent)
                finish()
            }

            val versionAPI = Build.VERSION.SDK_INT
            if (versionAPI < 26) {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                finish()
                            }
                        }
                    }

                val builder: androidx.appcompat.app.AlertDialog.Builder =
                    androidx.appcompat.app.AlertDialog.Builder(this)

                builder.setMessage("Your android version does not meet the criteria for this study. You can deinstall this app.")
                    .setPositiveButton("Ok", dialogClickListener).setCancelable(false)
                    .show()


            }

            var age = sharedPref.getString("age", "EMPTY")
            Log.e("age", age.toString())
            Log.e("SP", sharedPref.all.toString())
            /* if (age.toString() == "EMPTY"){
                 welcomeButton.setOnClickListener {
                     if (checkBox.isChecked){
                     /*    checkOtherPermissions()

     */
                         intent = Intent(this, PermissionActivity::class.java)
                         startActivity(intent)



                     }else{
                         Toast.makeText(this, "Please check the box at the end of the text", Toast.LENGTH_LONG)
                     }

                 }
             }else{
                 val intent = Intent(this, MainActivity::class.java)
                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                 startActivity(intent)
                 finish()
             }
     */


            var check = sharedPref.getBoolean("checkboxclicked", false)
            if (check == true) {
                Log.e("BUGFIX", "if checkboxclicked : $checkboxclicked")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                check = false
                //   val intent = Intent(this, WelcomeActivity::class.java)
                //  startActivity(intent)

            }
            welcomeButton.setOnClickListener {
                if (checkBox.isChecked) {
                    /*    checkOtherPermissions()

    */
                    intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)


                } else {
                    Toast.makeText(
                        this,
                        "Please check the box at the end of the text",
                        Toast.LENGTH_LONG
                    )
                }

            }

            val code = sharedPref.getString("CODE", "true")
            // todo still try to login? If database changes or something app thinks its registererd -> error
            if (code != null && code != "true") {
                Log.e("CODE", code.toString())
                val intent = Intent(this, MainActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                startActivity(intent)
                finish()
            }


        }else
        {
            setContentView(R.layout.activity_deleteapp)

        }



    }
    /*
        private fun checkOtherPermissions(){
            if (!getGrantStatusPower()){
                //Toast.makeText(this, "Usage Battery Permission not granted", Toast.LENGTH_LONG).show()
                intent = Intent(this, BatteryPermissionActivity::class.java)
                startActivity(intent)
            }
            else{Log.e("Permission", "1 granted")
                if(!getGrantStatusUA()){
                    Toast.makeText(this, "Usage Access Permission not granted", Toast.LENGTH_LONG).show()
                    intent = Intent(this, UsageAccessPermissionActivity::class.java)
                    startActivity(intent)
                }else{
                    Log.e("Permission", "2 granted")
                    if(!getGrantStatusOV()){
                    Toast.makeText(this, "Overlay Permission not granted", Toast.LENGTH_LONG).show()
                    intent = Intent(this, OverlayPermissionActivity::class.java)
                    startActivity(intent)
                    }else{
                    Log.e("Permission", "3 granted")
                    intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)}}}


        }
    */

    private fun checkOtherPermissions(){
        if (!getGrantStatusPower()){
            //Toast.makeText(this, "Usage Battery Permission not granted", Toast.LENGTH_LONG).show()
            intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
        }
        /* else{Log.e("Permission", "1 granted")
             if(!getGrantStatusUA()){
                 Toast.makeText(this, "Usage Access Permission not granted", Toast.LENGTH_LONG).show()
                 intent = Intent(this, UsageAccessPermissionActivity::class.java)
                 startActivity(intent)
             }else{
                 Log.e("Permission", "2 granted")
                 if(!getGrantStatusOV()){
                     Toast.makeText(this, "Overlay Permission not granted", Toast.LENGTH_LONG).show()
                     intent = Intent(this, OverlayPermissionActivity::class.java)
                     startActivity(intent)
                 }else{
                     Log.e("Permission", "3 granted")
                     intent = Intent(this, StartActivity::class.java)
                     startActivity(intent)}}}

 */ }

    private fun getGrantStatusPower(): Boolean {
        return powerManager.isIgnoringBatteryOptimizations(packageName)
        Settings.canDrawOverlays(this)
    }

    private fun getGrantStatusUA(): Boolean {
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

    private fun getGrantStatusOV(): Boolean {
        return Settings.canDrawOverlays(this)
    }


    /*fun startMainActivity(){
        var toast = Toast.makeText(this, "Main Service started", Toast.LENGTH_SHORT)
        toast.show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }*/
    override fun onDestroy() {
        super.onDestroy()

    }
}

