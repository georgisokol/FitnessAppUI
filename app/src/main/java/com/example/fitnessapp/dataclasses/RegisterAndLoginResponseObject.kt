package com.example.fitnessapp.dataclasses

import com.google.gson.annotations.SerializedName

data class RegisterAndLoginResponseObject (
    @SerializedName("message") var message: String
)
