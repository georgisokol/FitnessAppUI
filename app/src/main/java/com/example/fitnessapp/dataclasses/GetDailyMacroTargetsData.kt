package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class GetDailyMacroTargetsData (


    @SerializedName("createdOn") var createdOn :String,
    @SerializedName("deletedOn") var deletedOn :String,
    @SerializedName("uId") var Uid :String,
    @SerializedName("tProtein") var tProtein: Int ,
    @SerializedName("tCarbs") var tCarbs: Int ,
    @SerializedName("tFats") var tFats: Int ,
    @SerializedName("rProtein") var rProtein: Int ,
    @SerializedName("rCarbs") var rCarbs: Int ,
    @SerializedName("rFats") var rFats: Int ,
    @SerializedName("customMacros") var customMacros: Boolean
)
