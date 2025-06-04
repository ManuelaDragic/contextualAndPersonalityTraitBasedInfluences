package com.uniulm.social_media_interventions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DeleteApp: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleteapp)

        val sharedPref = getSharedPreferences("InfiniteScroll", 0)


    }
}