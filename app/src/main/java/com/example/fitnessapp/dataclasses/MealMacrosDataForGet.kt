package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class MealMacrosDataForGet (
    @SerializedName("uId") var Uid : String? ,
    @SerializedName("mealName") var mealName :String?,
    @SerializedName("protein") var mealProteins: Int = 0,
    @SerializedName("carbs") var mealCarbs: Int = 0,
    @SerializedName("fats") var mealFats: Int = 0

)
