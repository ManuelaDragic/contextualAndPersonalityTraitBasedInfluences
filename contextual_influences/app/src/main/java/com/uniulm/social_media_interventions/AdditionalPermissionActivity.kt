package com.uniulm.social_media_interventions

import android.Manifest
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_additional_perm.*
import kotlinx.android.synthetic.main.activity_start.*


class AdditionalPermissionActivity : AppCompatActivity() {




    lateinit var powerManager:PowerManager
    //var autoStartHelper: AutoStartHelper

    /***
     * Xiaomi
     */
    private val BRAND_XIAOMI = "xiaomi"
    private val PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter"
    private val PACKAGE_XIAOMI_COMPONENT =
        "com.miui.permcenter.autostart.AutoStartManagementActivity"

    /***
     * Letv
     */
    private val BRAND_LETV = "letv"
    private val PACKAGE_LETV_MAIN = "com.letv.android.letvsafe"
    private val PACKAGE_LETV_COMPONENT = "com.letv.android.letvsafe.AutobootManageActivity"

    /***
     * ASUS ROG
     */
    private val BRAND_ASUS = "asus"
    private val PACKAGE_ASUS_MAIN = "com.asus.mobilemanager"
    private val PACKAGE_ASUS_COMPONENT = "com.asus.mobilemanager.powersaver.PowerSaverSettings"

    /***
     * Honor
     */
    private val BRAND_HONOR = "honor"
    private val PACKAGE_HONOR_MAIN = "com.huawei.systemmanager"
    private val PACKAGE_HONOR_COMPONENT =
        "com.huawei.systemmanager.optimize.process.ProtectActivity"

    /**
     * Oppo
     */
    private val BRAND_OPPO = "oppo"
    private val PACKAGE_OPPO_MAIN = "com.coloros.safecenter"
    private val PACKAGE_OPPO_FALLBACK = "com.oppo.safe"
    private val PACKAGE_OPPO_COMPONENT =
        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK =
        "com.oppo.safe.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK_A =
        "com.coloros.safecenter.startupapp.StartupAppListActivity"

    /**
     * Vivo
     */
    private val BRAND_VIVO = "vivo"
    private val PACKAGE_VIVO_MAIN = "com.iqoo.secure"
    private val PACKAGE_VIVO_FALLBACK = "com.vivo.perm;issionmanager"
    private val PACKAGE_VIVO_COMPONENT = "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK =
        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK_A =
        "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"

    /**
     * Nokia
     */
    private val BRAND_NOKIA = "nokia"
    private val PACKAGE_NOKIA_MAIN = "com.evenwell.powersaving.g3"
    private val PACKAGE_NOKIA_COMPONENT =
        "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_perm)
        powerManager = applicationContext.getSystemService(POWER_SERVICE) as PowerManager

        this.button_submit2.isEnabled = false
        this.button_submit2.isClickable = false
        //autoStartHelper = AutoStartHelper()


    }

    override fun onBackPressed() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onStart() {
        super.onStart()


        //var intent = Intent(this, UsageAccessPermissionActivity::class.java)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (checkOtherPermissions()){

                findViewById<Button>(R.id.button_submit).setOnClickListener {
                    getASP()
                    this.button_submit2.isEnabled = true
                    this.button_submit2.isClickable = true
                    this.button_submit.isEnabled = false
                    this.button_submit.isClickable = false
                    findViewById<Button>(R.id.button_submit2).setOnClickListener {
                        val i = Intent(this, StartActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }

                //getASP()
                //getAutoStartPermission(this)

                //autoStartHelper = AutoStartHelper.instance.getAutoStartPermission(this) as AutoStartHelper
                //autoStartHelper.getAutoStartPermission(this)



            }

        }else{
            Toast.makeText(this, "Your Android Version does not meet the minimum Requirements to continue this study!", Toast.LENGTH_LONG).show()
        }




    }


    private fun checkOtherPermissions(): Boolean {
        if (!getGrantStatusPower()){
            Toast.makeText(this, "Usage Battery Permission not granted", Toast.LENGTH_LONG).show()
            intent = Intent(this, BatteryPermissionActivity::class.java)
            startActivity(intent)
            return false
             }
            else{Log.e("Permission", "1 granted")
            if(!getGrantStatusUA()){
                Toast.makeText(this, "Usage Access Permission not granted", Toast.LENGTH_LONG).show()
                intent = Intent(this, UsageAccessPermissionActivity::class.java)
                startActivity(intent)
                return false
            }else
                Log.e("Permission", "2 granted")
            if(!getGrantStatusOV()){
                Toast.makeText(this, "Overlay Permission not granted", Toast.LENGTH_LONG).show()
                intent = Intent(this, OverlayPermissionActivity::class.java)
                startActivity(intent)
                return false
            }else{
                Log.e("Permission", "3 granted")
                return true}}


    }

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


    fun getASP(){
        try {
            //Open the specific App Info page:
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + this.getPackageName())
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //Open the generic Apps page:
            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
            startActivity(intent)
        }


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
        Log.e("Additional permissions", "Granted!")
        intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }

}