package com.example.fitnessapp.repositories

import android.app.Application
import com.example.fitnessapp.dataclasses.RegisterAndLoginResponseObject
import com.example.fitnessapp.dataclasses.UserAndPassObject
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import retrofit2.Response

class RegisterAndLoginRepository(application : Application) {

    val dailyMacroTargetsService = ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

    suspend fun registerUser(user : UserAndPassObject) : Response<RegisterAndLoginResponseObject> {
        return dailyMacroTargetsService.registerUser(user)
    }

    suspend fun loginUser(user : UserAndPassObject) : Response<RegisterAndLoginResponseObject> {
        return dailyMacroTargetsService.loginUser(user)
    }
}