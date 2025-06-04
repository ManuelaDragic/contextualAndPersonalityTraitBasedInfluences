package com.uniulm.social_media_interventions

import android.Manifest
import android.app.*
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings.*
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_finalquestionnaire.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    //  private val CHANNEL_ID = "channel_id_example_01"
    var startstudy = Calendar.getInstance()
    var app_destroyed = false
    val CHANNEL_ID = "thanks"
    var notificationId = 1210
    val startStudyDateFormat = SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault())
    val formattedStartStudy = startStudyDateFormat.format(startstudy.time)

    var last_checkout_format = ""
    var pid_val = ""

    @RequiresApi(Build.VERSION_CODES.N)

    val reciever: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            setContentView(R.layout.activity_finalquestionnaire)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // createNotificationChannel()
        super.onCreate(savedInstanceState)
        // val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        //finalquest_started= sharedPref.getBoolean("finalquest_started", false)

        val sharedPref = getSharedPreferences("InfiniteScroll", 0)

        var finalquest_started = sharedPref.getBoolean("finalquest_started", false)

        //  sharedPref.edit().putBoolean("finalquest_main", finalquest_started).apply()
        /*  if(!finalquest_started) {
              setContentView(R.layout.activity_main)
              Log.e("destroy", "onCreate should be false : $finalquest_started")

              val notificationIntent = Intent(applicationContext, MainActivity::class.java)
              val pendingIntent = PendingIntent.getActivity(
                  applicationContext,
                  0, notificationIntent, 0 or PendingIntent.FLAG_IMMUTABLE
              )
              val builder = Notification.Builder(applicationContext, CHANNEL_ID)
                  .setContentTitle("InfinteScape")
                  .setContentText("Thank you for participating in this study. You can quit anytime by deleting the app")
                  .setSmallIcon(R.drawable.ic_stat_name)
                  .setContentIntent(pendingIntent)
                  .setOngoing(true)
                  .setVisibility(Notification.VISIBILITY_PUBLIC)
                  .setFullScreenIntent(pendingIntent, true)

              val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                  description = descriptionText
              }

              /*   with(NotificationManagerCompat.from(context)) {

                     notify(notificationId, builder.build())
                 }*/
              val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
              notificationManager.notify(notificationId, builder.build())
              notificationManager.createNotificationChannel(channel)
          }

          else if(finalquest_started) {

              Log.e("destroy", "contentview starting")
              // setContentView(R.layout.activity_finalquestionnaire)
              setContentView(R.layout.activity_finalquestionnaire)
              val name = "Last Questionnaire"
              val importance = NotificationManager.IMPORTANCE_HIGH
              val descriptionText = "Click to open the last questionnaire"
              val questionnaireIntent = Intent(applicationContext, FinalQuestionnaire::class.java).apply {
                  flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
              }

              val pendingIntent = PendingIntent.getActivity(applicationContext, 0, questionnaireIntent, 0 or PendingIntent.FLAG_IMMUTABLE)
              val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                  .setSmallIcon(R.drawable.ic_stat_name)
                  .setContentTitle("Last Questionnaire")
                  .setContentText("Click to confirm that you have completed the study")
                  .setContentIntent(pendingIntent)
                  .setPriority(NotificationCompat.PRIORITY_HIGH)
                  .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                  .setFullScreenIntent(pendingIntent, true)
                  .setOngoing(true)

              val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                  description = descriptionText
              }

              with(NotificationManagerCompat.from(applicationContext)) {
                  notify(notificationId, builder.build())
              }

              val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

              notificationManager.createNotificationChannel(channel)

              finalquest_started=true
              sharedPref.edit().putBoolean("finelquest_started",finalquest_started);

          }
          else */


        if (!finalquest_started) {
            Log.e("destroy", "appdestroyed and finalquest not there yet in onCreate()")
            Log.e("destroy", "onCreate should be falsee : $finalquest_started")

            if (app_destroyed) {
                setContentView((R.layout.all_permissions))

                val notificationIntent = Intent(applicationContext, PermissionActivity::class.java)
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                sharedPref.edit().putBoolean("app_destroyed", app_destroyed).apply()

                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0, notificationIntent, 0 or PendingIntent.FLAG_IMMUTABLE
                )

                val builder = Notification.Builder(applicationContext, CHANNEL_ID)
                    .setContentTitle("InfiniteScape")
                    .setContentText("Please click here and check if all permissions are still given")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setAutoCancel(false)

                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                /*   with(NotificationManagerCompat.from(context)) {

                   notify(notificationId, builder.build())
               }*/
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.notify(notificationId, builder.build())
                notificationManager.createNotificationChannel(channel)

            }

            else {
                setContentView((R.layout.activity_main))
                val notificationIntent = Intent(applicationContext, MainActivity::class.java)

                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0, notificationIntent, 0 or PendingIntent.FLAG_IMMUTABLE
                )

                val builder = Notification.Builder(applicationContext, CHANNEL_ID)
                    .setContentTitle("InfiniteScape")
                    .setContentText("Thank you for participating in this study. You can quit anytime by deleting the app")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setAutoCancel(false)
                    .setFullScreenIntent(pendingIntent, true)

                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                /*   with(NotificationManagerCompat.from(context)) {

                       notify(notificationId, builder.build())
                   }*/

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationId, builder.build())
                notificationManager.createNotificationChannel(channel)
            }
        }

        else if (finalquest_started) {
            Log.e("destroy", "appdestroyed and finalquest done in OnCreate ")
            Log.e("destroy", "else if onCreate should be true: $finalquest_started")
            setContentView(R.layout.activity_finalquestionnaire)
            val name = "Study completed"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val descriptionText = "Click to confirm that you have completed the study"
            val questionnaireIntent =
                Intent(applicationContext, FinalQuestionnaire::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                questionnaireIntent,
                0 or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Study completed")
                .setContentText("Click to confirm that you have completed the study")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
                .setOngoing(true)
                .setAutoCancel(false)

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

            finalquest_started = true
            sharedPref.edit().putBoolean("finalquest_started", finalquest_started);

            if (app_destroyed) {

                setContentView(R.layout.activity_finalquestionnaire)
                val name = "Study completed"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val descriptionText = "Click to open the last questionnaire"
                val questionnaireIntent =
                    Intent(applicationContext, FinalQuestionnaire::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                val pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    questionnaireIntent,
                    0 or PendingIntent.FLAG_IMMUTABLE
                )

                val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Study completed")
                    .setContentText("Click to confirm that you have completed the study")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setOngoing(true)
                    .setAutoCancel(false)


                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(notificationId, builder.build())
                }

                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.createNotificationChannel(channel)

                //  finalquest_started=true
                // sharedPref.edit().putBoolean("finelquest_started",finalquest_started);

            }
        }

        //quitButton.getBackground().setAlpha(50);
        var intent = Intent(this, AppCheckerService::class.java)

        var started = sharedPref.getBoolean("startstudy", false)
        //  Log.e("STUDYTIMER","MAIN BEFORE"+startstudy)

        if (started == false) {
            Log.e("BUGFIX", "timer hasnt started yet: $started")
            val timerServiceIntent = Intent(this, TimerService::class.java)
            startService(timerServiceIntent)
        }

        val startStudyDateFormat = SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault())
        val formattedStartStudy = startStudyDateFormat.format(startstudy.time)

        val editor = sharedPref.edit()
        Log.d("TimerService", "STUDY START: $formattedStartStudy")
        editor.putString("startStudy", formattedStartStudy)
        editor.apply()

        // Check if permissions are given
        if (getGrantStatus()) {
            // Check if the service is already running. If not start the foreground service
            if (!isAppCheckerServiceRunning()) {
                //quitButton.setEnabled(false)
                // Start the app checker service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(intent);
                }
            }
        }

        /*    val sharedPref = getSharedPreferences("InfiniteScroll", 0)
            val isRegistered = sharedPref.getBoolean("Registered", false)
            val code = sharedPref.getString("TEMP_CODE", "true")
            val helper = Helper()

            // Set study end timer should it be missing
            if (sharedPref.getString("STUDY_END_TIMER", "DEFAULT") == "DEFAULT") {
                val editor: SharedPreferences.Editor = sharedPref.edit()
                val date = Date()
                date.time = (date.time + 7 * 24 * 60 * 60 * 1000) // todo change to 7 days
                editor.putString("STUDY_END_TIMER", date.toString())
                editor.apply()
            }

            if (!isRegistered) {
                val editor: SharedPreferences.Editor = sharedPref.edit()
                // Set the end of the study
                val date = Date()
                date.time = (date.time + 7 * 24 * 60 * 60 * 1000)
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

            }*/

    }

    /*fun changequitButton(view: View?) {
        if (quitcheckBox.isChecked) {
            quitButton.setEnabled(true);
            quitButton.getBackground().setAlpha(255);
        }
        else {
            quitButton.setEnabled(false);
            quitButton.getBackground().setAlpha(50);
        }
    }*/

    /**
     * Converts the time stamp to a usable time object.
     */

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

        }

        else {
            mode == MODE_ALLOWED
        }
    }

    /**
     * Creates the Channel used for notifications.
     */
    /* private fun createNotificationChannel() {
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
                 getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)
         }
     }*/

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

    fun changetoQuestionnaire(view: View) {
        val intent = Intent(this, rhsci_activity::class.java)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
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

        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this)

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

    override fun onDestroy() {
        super.onDestroy()

        app_destroyed = true
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        sharedPref.edit().putBoolean("app_destroyed", app_destroyed).apply()
        var finalquest = sharedPref.getBoolean("finalquest_started", false)

        if (finalquest) {
            Log.e("destroy", "ondestroy should be true: $finalquest")
            Log.e("destroy", "finalqueststarted in onDestroy()")
            setContentView(R.layout.activity_finalquestionnaire)
            val name = "Study completed"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val descriptionText = "Click to open the last questionnaire"
            val questionnaireIntent =
                Intent(applicationContext, FinalQuestionnaire::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                questionnaireIntent,
                0 or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Study completed")
                .setContentText("Click to confirm that you have completed the study")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
                .setOngoing(true)
                .setAutoCancel(false)

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            with(NotificationManagerCompat.from(applicationContext)) {

                notify(notificationId, builder.build())
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
            finalquest = true
            sharedPref.edit().putBoolean("finalquest_started", finalquest);


        }

        else if (!finalquest) {
            Log.e("destroy", "no finalqueststarted in onDestroy()")
            Log.e("destroy", "ondestroy should be false: $finalquest")
            setContentView(R.layout.all_permissions)
            val notificationIntent = Intent(applicationContext, PermissionActivity::class.java)
            val sharedPref = getSharedPreferences("InfiniteScroll", 0)
            sharedPref.edit().putBoolean("app_destroyed", app_destroyed).apply()
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0, notificationIntent, 0 or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = Notification.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("InfinteScape")
                .setContentText("Please click here and check if all permissions are still given")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(false)

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            /*   with(NotificationManagerCompat.from(context)) {

               notify(notificationId, builder.build())
           }*/

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId, builder.build())
            notificationManager.createNotificationChannel(channel)

            app_destroyed = true
            sendBroadcast(Intent("YouWillNeverKillMe"))

            Log.e("AppDestroy", "true")
            /*if(comp == true && permissionsgiv == true){
            setContentView(R.layout.activity_main)
            finish()
        }*/
        }

    }

}


