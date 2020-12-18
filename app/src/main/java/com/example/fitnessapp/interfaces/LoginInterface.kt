package com.example.fitnessapp.interfaces

interface LoginInterface {
    fun onLoginSuccess(message :String)
    fun onFailureUsernameDoesNotExist(message: String)
    fun onFailureWrongPassword(message: String)
}