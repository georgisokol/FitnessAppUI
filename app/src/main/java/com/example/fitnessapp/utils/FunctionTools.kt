package com.example.fitnessapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun showFragment (fragmentId :Int, fragment: Fragment, tag: String, activity: AppCompatActivity) {
    activity.supportFragmentManager.beginTransaction()
        .replace(fragmentId, fragment, tag).addToBackStack(tag).commit()

}
fun hideKeyboardFrom(context: Context, view: View) {
    val imm =
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

