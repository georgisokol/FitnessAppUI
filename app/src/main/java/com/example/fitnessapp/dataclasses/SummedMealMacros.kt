package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class SummedMealMacros (
    @SerializedName("protein") var protein:Int = 0,
    @SerializedName("carbs") var carbs :Int = 0,
    @SerializedName("fats") var fats :Int = 0
)
