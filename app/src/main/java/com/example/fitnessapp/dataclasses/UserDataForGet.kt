package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class UserDataForGet (
    @SerializedName("uId") var Uid:String ,
    @SerializedName("age") var age: Int = 0,
    @SerializedName("gender") var gender: Int = 0,
    @SerializedName("height") var height: Int = 0,
    @SerializedName("weight") var weight: Int = 0,
    @SerializedName("frequency") var exerciseFrequency: Int = 0,
    @SerializedName("type") var exerciseType: Int = 0,
    @SerializedName("goal") var exerciseGoal: Int = 0
)
