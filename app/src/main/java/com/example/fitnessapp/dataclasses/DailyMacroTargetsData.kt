package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class DailyMacroTargetsData(
    @SerializedName("TProtein") var tProtein: Int = 0,
    @SerializedName("TCarbs") var tCarbs: Int = 0,
    @SerializedName("TFats") var tFats: Int = 0,
    @SerializedName("RProtein") var rProtein: Int = 0,
    @SerializedName("RCarbs") var rCarbs: Int = 0,
    @SerializedName("RFats") var rFats: Int = 0,
    @SerializedName("CustomMacros") var customMacros: Boolean = true

)
