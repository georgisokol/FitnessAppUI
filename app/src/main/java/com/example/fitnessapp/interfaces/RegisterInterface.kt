package com.example.fitnessapp.interfaces

import com.example.fitnessapp.dataclasses.RegisterAndLoginResponseObject

interface RegisterInterface {
    fun onSuccess(message :String)
    fun onFailureUsernameTaken(message: String)
}