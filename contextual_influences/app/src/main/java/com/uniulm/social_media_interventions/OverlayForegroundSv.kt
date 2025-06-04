package com.uniulm.social_media_interventions

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button

class OverlayForegroundService : Service() {

    // Other variables and methods

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Initialize and show overlay
        showOverlay()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the overlay if the service is stopped
        removeOverlay()
    }

    private fun showOverlay() {


    }

    private fun removeOverlay() {

    }

    // Other methods and classes for communication with AccessibilityService

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

