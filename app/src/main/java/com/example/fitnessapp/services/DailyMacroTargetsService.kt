package com.example.fitnessapp.services

import com.example.fitnessapp.dataclasses.*
import retrofit2.Call
import retrofit2.http.*

interface DailyMacroTargetsService {


    @POST("dailymacrotargets")
    fun addActiveDailyMacroTargets(@Body dailyMacrosTargetsData :DailyMacroTargetsData) : Call<DailyMacroTargetsData>

    @GET("dailymacrotargets")
    fun getActiveDailyMacroTargets() :Call<GetDailyMacroTargetsData>

    @PUT("dailymacrotargets/{Uid}")
    fun updateActiveDailyMacroTargets(@Path("Uid") Uid :String, @Body dailyMacrosTargetsData: DailyMacroTargetsData) :Call<DailyMacroTargetsData>

    @POST("users")
    fun addUser(@Body userData :UserDataForCreation) :Call<UserDataForCreation>

    @GET("users")
    fun getUser() :Call<UserDataForGet>

    @PUT("users/{Uid}")
    fun updateUser(@Path("Uid") Uid:String, @Body userData: UserDataForCreation) :Call<UserDataForCreation>

    @GET("dailymeals")
    fun getListOfDailyMealMacros() :Call<List<MealMacrosDataForGet>>

    @POST("dailymeals")
    fun addMealMacros(@Body mealMacrosForPost :MealMacrosForPost) :Call<MealMacrosForPost>

    @PUT("dailymeals/{Uid}")
    fun updateMealMacros(@Path("Uid") Uid: String, @Body mealMacrosForUpdate: MealMacrosForPost) :Call<MealMacrosForPost>

    @DELETE("dailymeals/{Uid}")
    fun deleteMealMacros(@Path("Uid") Uid: String) :Call<Unit>

    @GET("dailymeals/summed")
    fun getDailyMacrosIntakeSummed() :Call<SummedMealMacros>


    @GET("savedmeals")
    fun getSavedMeals() :Call<List<SavedMealForGet>>

    @POST("savedmeals")
    fun addMealToSavedMeals(@Body mealToAdd: SavedMealsForCreation) :Call<SavedMealsForCreation>

    @GET("dailymeals/history/{month}")
    fun getMealsHistory(@Path("month") month: String) :Call<List<MealsHistoryForGet>>
}