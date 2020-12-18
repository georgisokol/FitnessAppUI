package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class UserDataForGet (
    @SerializedName("uId") var Uid:String ,
    @SerializedName("age") var age: Int?,
    @SerializedName("gender") var gender: Int?,
    @SerializedName("height") var height: Int?,
    @SerializedName("weight") var weight: Int?,
    @SerializedName("frequency") var exerciseFrequency: Int?,
    @SerializedName("type") var exerciseType: Int?,
    @SerializedName("goal") var exerciseGoal: Int?
)
