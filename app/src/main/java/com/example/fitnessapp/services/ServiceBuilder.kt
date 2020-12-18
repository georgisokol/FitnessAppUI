package com.example.fitnessapp.services

import com.example.fitnessapp.application.MyApplication
import com.example.fitnessapp.network.ConnectivityInterceptor
import com.example.fitnessapp.network.ConnectivityInterceptorImpl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
    private const val URL = "http://10.0.2.2:5000/api/"

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttp = OkHttpClient.Builder().addInterceptor(ConnectivityInterceptorImpl(MyApplication.appContext!!)).addInterceptor(logger)

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val builder =
        Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).client(
            okHttp.build()
        )

    private val retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }


}