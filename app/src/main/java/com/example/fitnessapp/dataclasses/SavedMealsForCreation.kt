package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class SavedMealsForCreation (
    @SerializedName("Protein") var savedProtein :Int ,
    @SerializedName("Carbs") var savedCarbs: Int ,
    @SerializedName("Fats") var savedFats :Int,
    @SerializedName("MealName") var mealName :String
)

