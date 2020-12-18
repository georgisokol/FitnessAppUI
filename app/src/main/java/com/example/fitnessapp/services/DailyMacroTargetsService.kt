package com.example.fitnessapp.services

import com.example.fitnessapp.dataclasses.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DailyMacroTargetsService {

    //DAILY MACRO TARGETS
    @POST("macros/{userUid}/dailymacrotargets")
    fun addActiveDailyMacroTargets(@Path("userUid") userUid: String, @Body dailyMacrosTargetsData :DailyMacroTargetsData) : Call<DailyMacroTargetsData>

    @GET("macros/{userUid}/dailymacrotargets")
    suspend fun getActiveDailyMacroTargets(@Path("userUid") userUid :String) :Response<GetDailyMacroTargetsData>

    @PUT("macros/{userUid}/dailymacrotargets/{Uid}")
    fun updateActiveDailyMacroTargets(@Path("userUid") userUid: String,@Path("Uid") Uid :String, @Body dailyMacrosTargetsData: DailyMacroTargetsData) :Call<DailyMacroTargetsData>

    @PUT("macros/{userUid}/dailymacrotargets/{Uid}/training/{isTrainingDay}")
    suspend fun updateTrainingDay(@Path("userUid") userUid: String,@Path("Uid") Uid :String, @Path("isTrainingDay") isTrainingDay : Boolean) :Response<GetDailyMacroTargetsData>

    // USER PERSONAL INFO
    @GET("users/{userUid}")
    fun getUser(@Path("userUid")userUid: String) :Call<UserDataForGet>

    @PUT("users/{userUid}")
    fun updateUser(@Path("userUid") userUid:String, @Body userData: UserDataForCreation) :Call<UserDataForCreation>



    // DAILY MEALS

    @GET("macros/{userUid}/dailymeals")
    fun getListOfDailyMealMacros(@Path("userUid") userUid: String) :Call<List<MealMacrosDataForGet>>

    @POST("macros/{userUid}/dailymeals")
    fun addMealMacros(@Path("userUid")userUid: String,@Body mealMacrosForPost :MealMacrosForPost) :Call<MealMacrosDataForGet>

    @PUT("macros/{userUid}/dailymeals/{Uid}")
    fun updateMealMacros(@Path("userUid") userUid: String,@Path("Uid") Uid: String, @Body mealMacrosForUpdate: MealMacrosForPost) :Call<MealMacrosDataForGet>

    @DELETE("macros/{userUid}/dailymeals/{Uid}")
    fun deleteMealMacros(@Path("userUid") userUid: String,@Path("Uid") Uid: String) :Call<Unit>

    @GET("macros/{userUid}/dailymeals/summed")
    suspend fun getDailyMacrosIntakeSummed(@Path("userUid") userUid: String) :Response<SummedMealMacros>

    @GET("macros/{userUid}/dailymeals/history/{month}")
    suspend fun getMealsHistory(@Path("userUid") userUid: String,@Path("month") month: String) :Response<List<MealsHistoryForGet>>

    // SAVED MEALS
    @GET("savedmeals/{userUid}")
    fun getSavedMeals(@Path("userUid") userUid: String) :Call<List<SavedMealForGet>>

    @POST("savedmeals/{userUid}")
    fun addMealToSavedMeals(@Path("userUid") userUid: String,@Body mealToAdd: SavedMealsForCreation) :Call<SavedMealsForCreation>




    //LOGIN AND REGISTER
    @POST("users")
    suspend fun registerUser(@Body usernameAndPasswordObject : UserAndPassObject) : Response<RegisterAndLoginResponseObject>


    @POST("users/login")
    suspend fun loginUser(@Body usernameAndPasswordObject : UserAndPassObject) :Response<RegisterAndLoginResponseObject>
}