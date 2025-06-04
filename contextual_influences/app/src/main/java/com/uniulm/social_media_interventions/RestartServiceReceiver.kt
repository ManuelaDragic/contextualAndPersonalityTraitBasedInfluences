package com.uniulm.social_media_interventions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class RestartServiceReceiver:BroadcastReceiver() {
    val TAG = "RestartServiceReceiver"



    override fun onReceive(context: Context, intent: Intent?) {
        Log.e(TAG, "onReceive")
        context.startService(Intent(context.applicationContext, MainActivity::class.java))



      /*  if (intent!!.action != null && intent!!.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Restart a background service
            val serviceIntent = Intent(context, MainActivity::class.java)
            context.startService(serviceIntent)
        }*/

    }

}