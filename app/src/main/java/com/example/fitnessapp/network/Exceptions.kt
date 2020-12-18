package com.example.fitnessapp.network

import java.io.IOException

class NoConnectivityException : IOException() {

    override val message: String
        get() = "No Internet Connection or Server Down"

}