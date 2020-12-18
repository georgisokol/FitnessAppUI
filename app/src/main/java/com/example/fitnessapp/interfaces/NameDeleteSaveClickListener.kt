package com.example.fitnessapp.interfaces

import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.fitnessapp.dataclasses.MealMacrosDataForGet

interface NameDeleteSaveClickListener {
    fun giveNameToMeal(input: EditText, name : TextView, saveButton: ImageButton, mealMacros: MealMacrosDataForGet, cancelButton: ImageButton )
    fun deleteMeal(uId: String?, position :Int)
    fun saveMeal(mealName:TextView, protein :EditText, carbs: EditText, fats: EditText)
    fun addAndPostMeal(mealName : String, proteins: String, carbs: String, fats: String , uId: String?, mealProteinInput: EditText, mealCarbsInput: EditText, mealFatsInput: EditText, position :Int)
}