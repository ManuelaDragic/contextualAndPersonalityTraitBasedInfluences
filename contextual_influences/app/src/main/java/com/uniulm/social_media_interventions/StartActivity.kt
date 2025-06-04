package com.uniulm.social_media_interventions


import android.Manifest
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_situation.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_start.radioButton1
import kotlinx.android.synthetic.main.activity_start.radioButton3
import kotlinx.android.synthetic.main.activity_start.radioButton5
import kotlinx.android.synthetic.main.activity_start.startBtn
import kotlinx.android.synthetic.main.all_permissions.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

var pID=""
var startstudy=0L
var timestamp = ""
val CHANNEL_ID = "thanks"
val name = "Study completed"
val importance = NotificationManager.IMPORTANCE_HIGH
val descriptionText = "Click to open the last questionnaire"

var notificationId = 1210
var isQuestionnaireCompleted  = false

class StartActivity : AppCompatActivity(){


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
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






        // As some methods require a certain version, we can't support them and try to
        // exclude them from the start via a information dialog
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

            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this)

            builder.setMessage("Your android version does not meet the criteria for this study. You can deinstall this app.")
                .setPositiveButton("Ok", dialogClickListener).setCancelable(false)
                .show()
        }

        // val sharedPref = getSharedPreferences("InfiniteScroll", 0)

        // If the code is already stored in shared pref, the user is already logged in -> Guide them to the main
        val code = sharedPref.getString("CODE", "true")
        val comp= sharedPref.getBoolean(isQuestionnaireCompleted.toString(),false)
        // todo still try to login? If database changes or something app thinks its registererd -> error
        /*  if (code != null && code != "true") {
              val intent = Intent(this, MainActivity::class.java)
              startActivity(intent)
              finish()
          }*/

        var permissionsgiven= true

        sharedPref.edit().putBoolean("permissionsgiven", permissionsgiven).apply()

        if(isQuestionnaireCompleted==true){

            Log.e("BUGFIX", "if questionnairecompleted : $isQuestionnaireCompleted")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        startBtn.setOnClickListener {
            // Check if the prolific id is correct and exists
            if(prolificID_text.text.toString().isEmpty()){
                Toast.makeText(
                    this,
                    "Please enter your Prolific ID", Toast.LENGTH_SHORT
                ).show()
            }

            else if(codeEdit2.text.toString().isEmpty()){
                Toast.makeText(
                    this,
                    "Please enter your Age correctly", Toast.LENGTH_SHORT
                ).show()
            }

            else if(!radioButton.isChecked&&!radioButton5.isChecked&&!radioButton3.isChecked&&!radioButton1.isChecked){Toast.makeText(
                this,
                "Please choose a Gender", Toast.LENGTH_SHORT
            ).show()
            } else {


                try {
                    // generateCSV()

                    sendData()
                } catch (e: IOException) {
                    e.printStackTrace()
                }





                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                isQuestionnaireCompleted=true
                sharedPref.edit().putBoolean("COMPLETE", isQuestionnaireCompleted)
                /* val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                 sharedPref.edit().putString("pID", pID).apply()
                  */
                /*


                               sharedPref.getLong("START STUDY TIME", startstudy)
                               Log.e("STUDYTIMER","STARTACTIVITY BEFORE")
                               val timerServiceIntent = Intent(this, TimerService::class.java)
                               startService(timerServiceIntent)
                               Log.e("STUDYTIMER","STARTACTIVITY AFTER")*/

                /*  savedInstanceState?.let {

                      isQuestionnaireCompleted = it.getBoolean("isQuestionnaireCompleted", false)
                      if(isQuestionnaireCompleted){
                          startActivity(intent)

                      }
                  }*/





            }
        }

    }
    fun getCurrentLanguage(): String {
        // Get the default Locale of the device
        val locale = Locale.getDefault()
        // Get the language code (e.g., "en" for English, "es" for Spanish)
        return locale.language
    }
    data class InitialAnswers(
        val pID: String,
        val sex: String,
        val age: String,
        val AndroidID: String,
        val language : String,
        val androidVersion:String,
        val manufacturer:String,
        val timestamp:String
    )
    data class FinalTimer(
        val startstudy: Long,
    )

    fun sendData(){
        val db = FirebaseFirestore.getInstance()
        val pID= prolificID_text.text.toString()
        val age = codeEdit2.text.toString()
        Log.e("Age", age)
        Log.e("pID", pID)
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        sharedPref.edit().putString("pID", pID).apply()

        val editor: SharedPreferences.Editor = sharedPref.edit()

        var sdf_1: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMM d HH:mm:ss", Locale.getDefault())
        var formattedTimestamp = sdf.format(sdf_1.time)
        editor.putString("timestamp", formattedTimestamp)
        editor.putString("pID", pID.toString())
        editor.putString("age", age.toString())
        editor.apply()
        Log.e("SP", sharedPref.all.toString())

        timestamp = sharedPref.getString("timestamp", "").toString()
        var gen:String
        val androidVersion = "Android " + Build.VERSION.RELEASE.toString()
        val manufacturer = Build.MANUFACTURER.toString()
        if(radioButton1.isChecked){
            gen = "Male"
        }
        else if(radioButton3.isChecked){
            gen = "Female"

        }
        else if(radioButton5.isChecked){
            gen = "Non-binary"

        }
        else{//radioButton.isChecked
            gen = "Prefer not to answer"}

        val dID = Secure.getString(contentResolver, Secure.ANDROID_ID)
        val language= getCurrentLanguage()

        // val startstudy= startstudy
        //val gen = codeEdit3.text
        val initialAnswers = InitialAnswers(pID, gen, age, dID, language, androidVersion, manufacturer, timestamp)




        db.collection("initial_answers")
            .add(initialAnswers)
            .addOnSuccessListener { documentReference -> Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")}
            .addOnFailureListener { e ->Log.w("Firestore", "Error adding document", e) }

        /* db.collection("final")
             .add(finalTimer)
             .addOnSuccessListener { documentReference -> Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")}
             .addOnFailureListener { e ->Log.w("Firestore", "Error adding document", e) }*/
    }


    /*  override fun onSaveInstanceState(outState: Bundle) {
          super.onSaveInstanceState(outState)
          // Save your activity's state data into the outState bundle here
          outState.putBoolean("isQuestionnaireCompleted", isQuestionnaireCompleted)
          // Add other data as needed
      }*/

    private fun isAppCheckerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.uniulm.social_media_interventions.AppCheckerService" == service.service.className) {
                return true
            }
        }
        return false
    }

    /*   override fun onResume() {
           super.onResume()
           val sharedPref = getSharedPreferences("InfiniteScroll", 0)

           val comp= sharedPref.getBoolean("COMPLETE", false)
           if(comp == true){
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
               finish()
           }
       }

       override fun onDestroy() {
           super.onDestroy()
           val sharedPref = getSharedPreferences("InfiniteScroll", 0)

           val comp= sharedPref.getBoolean("COMPLETE", false)
           if(comp == true){
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
               finish()
           }
       }*/

}





