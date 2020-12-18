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

    fun getUserUid() :String? {
        return preference.getString(PREFERENCE_USER_UID, null)
    }

    fun setUserUid(userUid :String?) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_UID, userUid)
        editor.apply()
    }

    fun getDailyMacroTargetsUid() :String? {
        return preference.getString(PREFERENCE_DAILY_MACRO_TARGETS_UID, null)
    }

    fun setDailyMacroTargetsUid(uId :String?) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_DAILY_MACRO_TARGETS_UID, uId)
        editor.apply()
    }

    fun getRestDaySuggestion() :Boolean {
        return preference.getBoolean(PREFERENCE_REST_DAY_SUGGESTION,false)
    }
    fun setRestDaySuggestion(flag: Boolean) {
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_REST_DAY_SUGGESTION, flag)
        editor.apply()
    }

    fun getRestDaySuggestionAlreadyPopped() :Boolean {
        return preference.getBoolean(PREFERENCE_REST_DAY_SUGGESTION_POPPED, false)
    }

    fun setRestDaySuggestionAlreadyPopped(flag :Boolean) {
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_REST_DAY_SUGGESTION_POPPED, flag)
        editor.apply()
    }

    fun clearMyPreference () {
        preference.edit().clear().apply()
    }



}