package com.uniulm.social_media_interventions


import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_situation.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_start.radioButton1
import kotlinx.android.synthetic.main.activity_start.radioButton3
import kotlinx.android.synthetic.main.activity_start.radioButton5
import kotlinx.android.synthetic.main.activity_start.startBtn
import kotlinx.android.synthetic.main.activity_welcome.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.*


class SituationActivity : AppCompatActivity(){
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)



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

        val sharedPref = getSharedPreferences("InfiniteScroll", 0)

        // If the code is already stored in shared pref, the user is already logged in -> Guide them to the main
        val code = sharedPref.getString("CODE", "true")
        // todo still try to login? If database changes or something app thinks its registererd -> error
        if (code != null && code != "true") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        codeEdit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                radioButton5.text=radioButton3.text
                radioButton3.text=radioButton1.text
                radioButton1.setText(codeEdit.getText().toString())
            }

            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
            }
        })
        startBtn.setOnClickListener {
            // Check if the prolific id is correct and exists

            if(radioButton1.isChecked||radioButton3.isChecked||radioButton5.isChecked){
                val intent = Intent(this, ThankActivity::class.java)
                startActivity(intent)
                finish()
            }else{

            }



                // todo disable button until valid
                /*if (matches("[a-zA-Z0-9]{24}", codeEdit.text.toString())) {
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("TEMP_CODE", codeEdit.text.toString())
                    editor.apply()
                    val helper = Helper();
                    val activity = this;
                    val context = this;
                    val json = JSONObject()
                        .put("code", codeEdit.text.toString())
                        .put("DEVICE_ID", Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        ))

                    val firstQuestIntent = Intent(context, AgeActivity::class.java)

                    // Try to login -> If a token gets saved the user exists and we can redirect to the main activity
                    /*helper.sendRequest(
                        context,
                        object : VolleyCallBack {
                            override fun onSuccess(response: JSONObject?) {
                                val token = sharedPref.getString("TOKEN", "true")
                                if (token != null && token != "true") {
                                    Log.e("TOKEN", token.toString())
                                    editor.putBoolean("Registered", true)
                                    editor.putString("CODE", codeEdit.text.toString())
                                    editor.apply()
                                    val intent = Intent(context, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                            }

                            override fun onFailure(response: JSONObject?) {
                                Log.e("ERROR_RESPONSE", response.toString())
                                // Token does not exist -> Start first survey and register
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                                startActivity(firstQuestIntent)
                                                finish()
                                            }
                                        }
                                    }

                                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                                builder.setMessage("Hey, thank you for participating. In the following we will ask you a few questions regarding your social media consumption.").setPositiveButton("Continue", dialogClickListener).show()
                            }
                        },
                        "/login", "POST", json.toString()
                    )*/

                } else {
                    Toast.makeText(
                        this,
                        "Code not correct format", Toast.LENGTH_SHORT
                    ).show()
                }*/

        }

    }



    fun sendData(){

        val pid = codeEdit.text
        val age = codeEdit2.text
        var gen:String

        if(radioButton1.isChecked){
            gen = "Male"
        }else if(radioButton3.isChecked){
            gen = "Female"

        }else if(radioButton5.isChecked){
            gen = "Non-binary"

        }else{//radioButton.isChecked
             gen = "Prefer no to answer"}

        //val gen = codeEdit3.text


        val url = "http://134.60.152.201:5000/write_intervention_demo_csv"
        val username = "user"
        val password = "password"
        val credentials = Credentials.basic(username,password)
        val initObject = JSONObject()
        initObject.put("user_id", pid)
        initObject.put("sex", gen)
        initObject.put("age", age)

        Log.e("JSON", initObject.toString())
        postJsonToServer(url, initObject, pid.toString(), credentials){
            response ->
            Log.d("Response", response.toString())
        }
    }

    fun postJsonToServer(url: String, jsonObject: JSONObject, pid: String, credentials: String, callback: (response: String?) -> Unit) {
        val client = OkHttpClient()
        //val intent = Intent(this, WelcomeActivity::class.java)
        //var intent = Intent(this, AppCheckerService::class.java)
        val intent = Intent(this, MainActivity::class.java)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .addHeader("Authorization", credentials)
            .addHeader("Content-Type", "text/csv")
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody)

                //startActivity(intent)
                val sharedPref = getSharedPreferences("InfiniteScroll", 0)
                sharedPref.edit().clear().apply()
                sharedPref.edit().putString("pID", pid).apply()
                /*with (sharedPref.edit()) {
                    putString("pID", pid)
                    apply()
                }*/

                Log.e("pID", pid.toString())
                if (getGrantStatus()) {


                    startActivity(intent)
                    finish()



                    // Check if the service is already running. If not start the foreground service
                    /*if (!isAppCheckerServiceRunning()) {
                        //quitButton.setEnabled(false)
                        // Start the app checker service

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                            finish()
                        } else {
                            startService(intent)
                        }
                    }*/
                } else {


                    /*val builder: androidx.appcompat.app.AlertDialog.Builder =
                        androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setMessage("Please allow the usage stats permission and restart this app, as it will not work without it and you will not be able to finish this study.")
                        .setPositiveButton("Continue", dialogClickListener).setCancelable(false)
                        .show()*/
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }
        })
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

    private fun isAppCheckerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.uniulm.social_media_interventions.AppCheckerService" == service.service.className) {
                return true
            }
        }
        return false
    }




    fun generateCSV(){
        // Create a new CSV file
        val folder = filesDir
        val f = File(folder, "folder_name")
        f.mkdir()
        val filePath = File("/Android/data/com.uniulm.social_media_interventions/files/")
        //val filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator+"/Android/data/com.uniulm.social_media_interventions/files/"
        val csvFile = File(filePath, "id.csv")

        csvFile.mkdirs()
        csvFile.createNewFile()


        //ActivityCompat.requestPermissions(Environment.getExternalStorageDirectory())
        /*if (!csvFile.exists()){
            csvFile.mkdirs()
        }*/

        val csvData = listOf(
            listOf(codeEdit.text//, codeEdit2.text, codeEdit3.text
                )
        )

        //val csvFile = File(filePath, "id.csv")
        Log.e("FP", filePath.toString())
        val writer = BufferedWriter(FileWriter(csvFile))

        for (row in csvData) {
            val rowString = row.joinToString(",") // Convert row to comma-separated string
            writer.write(rowString)
            writer.newLine()
        }

        writer.flush()
        writer.close()
    }


}




