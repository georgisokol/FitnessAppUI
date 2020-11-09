package com.example.fitnessapp.utils

import android.content.Context

class MyPreference(context: Context) {
    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getFirstTimeOpeningApp() :String? {
        return preference.getString(PREFERENCE_FIRST_TIME_OPENING_APP, null)

    }
    fun setFirstTimeOpeningApp(string : String?) {
        val editor= preference.edit()
        editor.putString(PREFERENCE_FIRST_TIME_OPENING_APP, string)
        editor.apply()
    }

    fun  getTrainingOrRestDay() :String? {
        return preference.getString(PREFERENCE_TRAINING_OR_REST_DAY, null)
    }
    fun setTrainingOrRestDay(string :String?) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_TRAINING_OR_REST_DAY, string)
        editor.apply()

    }



}