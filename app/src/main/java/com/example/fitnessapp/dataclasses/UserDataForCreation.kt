package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class UserDataForCreation (
    @SerializedName("Age") var age :Int = 0,
    @SerializedName("Gender") var gender: Int = 0,
    @SerializedName("Height") var height: Int = 0,
    @SerializedName("Weight") var weight: Int = 0,
    @SerializedName("Frequency") var exerciseFrequency: Int = 0,
    @SerializedName("Type") var exerciseType: Int = 0,
    @SerializedName("Goal") var exerciseGoal: Int = 0
)
