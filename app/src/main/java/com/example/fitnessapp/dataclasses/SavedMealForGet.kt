package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class SavedMealForGet (
    @SerializedName("uId") var uId :String,
    @SerializedName("protein") var protein:Int,
    @SerializedName("carbs") var carbs :Int,
    @SerializedName("fats") var fats: Int,
    @SerializedName("mealName") var mealName:String
)
