package com.example.fitnessapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.utils.PREFERENCE_NAME

class OnClearFromRecentService : Service() {
     lateinit var myPreference: MyPreference


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ClearFromRecentService", "Service Started")
        myPreference = MyPreference(applicationContext)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ClearFromRecentService", "Service Destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("ClearFromRecentService", "END")

        myPreference.setTrainingOrRestDay(null)
        myPreference.setRestDaySuggestionAlreadyPopped(false)


    }

}