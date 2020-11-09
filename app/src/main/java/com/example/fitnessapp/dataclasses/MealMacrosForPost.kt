package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class MealMacrosForPost (
    @SerializedName("MealName") var mealName :String,
    @SerializedName("Protein") var mealProteins: Int = 0,
    @SerializedName("Carbs") var mealCarbs: Int = 0,
    @SerializedName("Fats") var mealFats: Int = 0
)
