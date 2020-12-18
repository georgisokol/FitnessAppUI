package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class UserAndPassObject (
    @SerializedName("Username") var username: String,
    @SerializedName("Password") var password: String
)
