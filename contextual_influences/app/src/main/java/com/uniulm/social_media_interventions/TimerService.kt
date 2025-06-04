package com.uniulm.social_media_interventions

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*
import java.text.SimpleDateFormat

class TimerService : Service() {
    var startstudy= 0L
    var sevdaytimer = false
    var finalquest_started=false
    var notificationId = 1210
    val CHANNEL_ID = "thanks"
    var sched=true
    companion object {
        private const val NOTIFICATION_ID = 1210
    }
   /* override fun onCreate() {
        super.onCreate()


        // Check if it's time to show the notification and create the notification if needed
        if (scheduleNotification()) {
            Log.e("destroy", "true schedulenotification")

            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)
        }
    }*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

       /* if (scheduleNotification()) {
            Log.e("destroy", "true schedulenotification")

            val notification = createNotification()
            startForeground(notificationId, notification)
        }*/

        scheduleNotification()

        // Return START_NOT_STICKY to indicate that the service should not be restarted if it's killed
        return START_NOT_STICKY
    }

   private fun scheduleNotification() {
        val sharedPreferences = getSharedPreferences( "InfiniteScroll", 0)
      //   startstudy = sharedPreferences.edit().putLong(startstudy.Calendar.getInstance())
        startstudy = Calendar.getInstance().timeInMillis


        Log.e("STUDYTIMER","TIMERSERVICE")
       // days * hours * minutes * seconds * milliseconds
//       val sevenDaystimer = 60 * 1000L // only 60 seconds for debugging
//       val sevenDaystimer = 10 * 60 * 1000L // only 10 Minutes for debugging
//       val sevenDaystimer = 30 * 60 * 1000L // 1/2 hour in milliseconds
//       val sevenDaystimer = 1 * 60 * 60 * 1000L // 1 hour in milliseconds
//       val sevenDaystimer = 2 * 60 * 60 * 1000L // 2 hours in milliseconds
//       val sevenDaystimer = 1L * 24 * 60 * 60 * 1000L // 1 day in milliseconds luca
//       val sevenDaystimer = 4L * 24 * 60 * 60 * 1000L // 4 days in milliseconds for test study
       val sevenDaystimer = 7L * 24 * 60 * 60 * 1000L // 7 days in milliseconds for study

        val triggerTimeMillis = sevenDaystimer + startstudy
        val notificationIntent = Intent(this, NotificationReceiver::class.java)

       val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
        Log.e("STUDYTIMER", "TIMERSERVICE AFTER")
        sevdaytimer = true
        sharedPreferences.edit().putBoolean("startstudy", sevdaytimer).apply()

        val formattedExpectedEndDate = convertMillisToDate(triggerTimeMillis)
        sharedPreferences.edit().putString("expectedEndStudy", formattedExpectedEndDate).apply()

        if(sevdaytimer){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
            //finalquest_started=true
            //val sharedPref = getSharedPreferences("InfiniteScroll", 0)
            //sharedPref.edit().putBoolean("finalquest_started", finalquest_started).apply()
        }
       Log.d("TimerService", "STUDY END: $formattedExpectedEndDate")

    }

    // convert Milliseconds to Date Format
    fun convertMillisToDate(millis: Long): String {
        val date = Date(millis)
        val format = SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

  /*  fun scheduleNotification():Boolean{
       val sharedPreferences = getSharedPreferences( "InfiniteScroll", 0)

       Log.e("destroy", "in schedulenotification")

       startstudy= Calendar.getInstance().timeInMillis
     // val currentTime = System.currentTimeMillis()
     // val elapsedTime = currentTime - startstudy
      // Check if the required time (7 days in this case) has passed
    //  val sevenDayMillis = 7 * 24 * 60 * 60 * 1000L // 7 days in milliseconds
        val sevenDayMillis = 30 * 1000L // 7 days in milliseconds
       val temp = 0L
       val trigger = startstudy +temp

       sevdaytimer = true
       sharedPreferences.edit().putBoolean("startstudy", sevdaytimer).apply()
     /*  if(sevdaytimer){

       }*/
      if(trigger==startstudy+sevenDayMillis){
          sched= true
      }else{
          sched=false
      }

       return sched
    }*/
    fun createNotification(): Notification {

        //  var channeld_id ="id_smi01"
        val name = "Last Questionnaire"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val descriptionText = "Click to open the last questionnaire"
        //  AppCheckerService.shouldStopOldNotification = true
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //  notificationManager.cancel(1)
        val questionnaireIntent = Intent(applicationContext, FinalQuestionnaire::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, questionnaireIntent, 0 or PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Study completed")
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

        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)


        Log.e("STUDYTIMER", "AFTER"+notificationId.toString()+ CHANNEL_ID.toString())

        finalquest_started=true
        val sharedPref = applicationContext.getSharedPreferences("InfiniteScroll", 0)
        sharedPref.edit().putBoolean("finalquest_started", finalquest_started).apply()

        return builder.build()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


