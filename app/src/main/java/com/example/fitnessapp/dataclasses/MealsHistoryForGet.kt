package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class MealsHistoryForGet (
    @SerializedName("createdOn") var creationDate : String,
    @SerializedName("protein") var protein: Int,
    @SerializedName("carbs") var carbs :Int ,
    @SerializedName("fats") var fats :Int
)
