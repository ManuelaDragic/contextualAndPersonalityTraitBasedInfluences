package com.uniulm.social_media_interventions

import android.Manifest
import android.app.*
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.APP_OPS_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.Process
import android.provider.Settings
import android.provider.Settings.*
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Delay
import org.json.JSONObject
import java.lang.System
import java.text.SimpleDateFormat
import java.util.*

class ThankActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_example_01"

    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank)
        //quitButton.getBackground().setAlpha(50);
        var intent = Intent(this, AppCheckerService::class.java)









        /*if (!isAppCheckerServiceRunning()) {
            quitButton.setEnabled(false)
            // Start the app checker service

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent)
            }
        }*/




        // Check if permissions are given
        if (getGrantStatus()) {
            // Check if the service is already running. If not start the foreground service
            if (!isAppCheckerServiceRunning()) {
                //quitButton.setEnabled(false)
                // Start the app checker service

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent)
                }
            }
        } else {


            /*val builder: androidx.appcompat.app.AlertDialog.Builder =
                androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Please allow the usage stats permission and restart this app, as it will not work without it and you will not be able to finish this study.")
                .setPositiveButton("Continue", dialogClickListener).setCancelable(false)
                .show()*/
        }


        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val isRegistered = sharedPref.getBoolean("Registered", false)
        val code = sharedPref.getString("TEMP_CODE", "true")
        val helper = Helper()

        // Set study end timer should it be missing
        if (sharedPref.getString("STUDY_END_TIMER", "DEFAULT") == "DEFAULT") {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            val date = Date()
            date.time = (date.time + 7L * 24 * 60 * 60 * 1000) // todo change to 7 days
            editor.putString("STUDY_END_TIMER", date.toString())
            editor.apply()
        }

        if (!isRegistered) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            // Set the end of the study
            val date = Date()
            date.time = (date.time + 7L * 24 * 60 * 60 * 1000)
            editor.putString("STUDY_END_TIMER", date.toString())
            editor.apply()

            //val questionnaireAnswers = sharedPref.getString("questionnaireAnswers", "")
            //val questionnaire = JSONObject(questionnaireAnswers)
            //questionnaire.put("deviceModel", Build.MANUFACTURER + " - " + Build.MODEL)
            //Log.e("Questionaire + Device", questionnaire.toString())
            val history = JSONObject()
                .put("history", showUsageStats("test").toString())
                .put("dailyHistory", showUsageStats("daily").toString())

            val activity = this;
            val context = this;

            /*helper.createNewUser(
                activity,
                context,
                questionnaire,
                history,
                code.toString(),
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )*/
        }

        // Opens the default email app to send a support email
        /*this.support.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.putExtra(Intent.EXTRA_SUBJECT, code.toString())
            intent.putExtra(Intent.EXTRA_TEXT, "How can we help?")
            intent.data = Uri.parse("mailto:beyinf.feedback@gmail.com")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }*/

        // Quits the study for the user, sets that flag on the server and stops the app
        /*this.quitButton.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            val editor: SharedPreferences.Editor = sharedPref.edit()
                            editor.putString("QUIT", "true")
                            editor.apply()
                            sendQuitStudy()
                            val intent = Intent(this, AppCheckerService::class.java)
                            stopService(intent)
                            finish()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
                .setMessage("Are you sure? You won't be able to complete the study.")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }*/
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent(this, AppCheckerService::class.java)
        /*if (!isAppCheckerServiceRunning()) {
            quitButton.setEnabled(false)
            // Start the app checker service

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent)
            }
        }*/
        // Check if permissions are given
        if (getGrantStatus()) {
            // Check if the service is already running. If not start the foreground service
            if (!isAppCheckerServiceRunning()) {
                //quitButton.setEnabled(false)
                // Start the app checker service

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent)
                }
            }
        } else {


            /*val builder: androidx.appcompat.app.AlertDialog.Builder =
                androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Please allow the usage stats permission and restart this app, as it will not work without it and you will not be able to finish this study.")
                .setPositiveButton("Continue", dialogClickListener).setCancelable(false)
                .show()*/
        }



    }


    /**
     * Gets and add the usage stats of the android device.
     */
    fun showUsageStats(getString: String?): String? {

        val usageStatsManager: UsageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -30)
        val queryUsageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_WEEKLY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )
        if (getString == "daily") {
            val queryUsageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                cal.timeInMillis,
                System.currentTimeMillis()
            )
        }
        var statsData: String = ""
        for (i in 0..queryUsageStats.size - 1) {
            if (queryUsageStats.get(i).totalTimeInForeground > 0) {
                if (packageIsRelevantApp(queryUsageStats.get(i).packageName)) {
                    statsData =
                        statsData + "Package Name : " + queryUsageStats.get(i).packageName + "\n" +
                                "Last Time Used : " + convertTime(queryUsageStats.get(i).lastTimeUsed) + "\n" +
                                "Total time in foreground : " + (queryUsageStats.get(i).totalTimeInForeground / 1000) + "seconds" + "\n" + "\n"
                }
            }
        }
//        usageStats.setText(statsData)
        if (getString != null) {
            return statsData
        }
        return null
    }

    /*fun changequitButton(view: View?) {
        if (quitcheckBox.isChecked) {

            quitButton.setEnabled(true);
            quitButton.getBackground().setAlpha(255);
        } else {

            quitButton.setEnabled(false);
            quitButton.getBackground().setAlpha(50);
        }

    }*/

    override fun onPause() {
        super.onPause()
        finish()
    }

    /**
     * Converts the time stamp to a usable time object.
     */
    private fun convertTime(lastTimeUsed: Long): String {
        val date = Date(lastTimeUsed)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        return format.format(date)
    }

    /**
     * Returns a boolean whether the current app is a relevant app.
     */
    fun packageIsRelevantApp(packageName: String): Boolean {
        return when (packageName) {
            "com.facebook.android" -> {
                true
            }
            "com.facebook.katana" -> {
                true
            }
            "com.instagram.android" -> {
                true
            }
            "com.reddit.frontpage" -> {
                true
            }
            "com.andrewshu.android.reddit" -> {
                true
            }
            "com.rubenmayayo.reddit" -> {
                true
            }
            "free.reddit.news" -> {
                true
            }
            "com.zhiliaoapp.musically" -> {
                true
            }
            "com.ninegag.android.app" -> {
                true
            }
            "com.pinterest" -> {
                true
            }
            "com.twitter.android" -> {
                true
            }
            else -> {
                false
            }
        }
    }

    /**
     * check if PACKAGE_USAGE_STATS permission is allowed for this application
     * @return true if permission granted
     */
    private fun getGrantStatus(): Boolean {
        val appOps = applicationContext
            .getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            OPSTR_GET_USAGE_STATS,
            Process.myUid(), applicationContext.packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            (applicationContext.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED) &&
                    (applicationContext.checkCallingOrSelfPermission((Manifest.permission.SYSTEM_ALERT_WINDOW)) == PackageManager.PERMISSION_GRANTED)

        } else {
            mode == MODE_ALLOWED
        }
    }

    /**
     * Creates the Channel used for notifications.
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notfication title"
            val descriptionText = "Notfication desc"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Checks if the service is already running
     */
    private fun isAppCheckerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.uniulm.social_media_interventions.AppCheckerService" == service.service.className) {
                return true
            }
        }
        return false
    }

    fun deleteSharedPrefs(view: View) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                        sharedPref.edit().remove("RequestQueue").apply()
                        sharedPref.edit().remove("CODE").apply()
                        sharedPref.edit().remove("Registered").apply()
                        sharedPref.edit().remove("NOTIFICATION_TIMER").apply()
                        sharedPref.edit().remove("REFRESH_TOKEN_TIMER").apply()
                        sharedPref.edit().remove("STUDY_END_TIMER").apply()
                        sharedPref.edit().remove("WhyStoppedOtherAnswers").apply()
                        val editor: SharedPreferences.Editor = sharedPref.edit()
                        editor.putString("QUIT", "true")
                        editor.apply()

                        val intent = Intent(this, AppCheckerService::class.java)
                        stopService(intent)
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {

                    }
                }
            }

        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(this)

        builder.setMessage("Delete Preferences?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendQuitStudy() {
        val helper = Helper()
        /*helper.sendRequest(
            this,
            object : VolleyCallBack {
                override fun onSuccess(response: JSONObject?) {
//                    Log.e("REFRESH RESPONSE", response.toString())
                }

                override fun onFailure(response: JSONObject?) {
//                    Log.e("ERROR_RESPONSE", response.toString())
                }
            },
            "/quit", "POST", null
        )*/
    }
}
