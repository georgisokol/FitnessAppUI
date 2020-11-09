package com.example.fitnessapp.repositories

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.dataclasses.*
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext


class MacrosRepository(application :Application) {
    val dailyMacroTargetsService  =
        ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

    fun getMealMacros() :MutableLiveData<MutableList<MealMacrosDataForGet>> {
        val mutableLiveData : MutableLiveData<MutableList<MealMacrosDataForGet>> = MutableLiveData()

        val mealMacrosRequestCall = dailyMacroTargetsService.getListOfDailyMealMacros()
        mealMacrosRequestCall.enqueue(object: Callback<List<MealMacrosDataForGet>> {
            override fun onResponse(
                call: Call<List<MealMacrosDataForGet>>,
                response: Response<List<MealMacrosDataForGet>>
            ) {
                if(response.isSuccessful){
                    val mealMacrosList = response.body()
                    val mealMacrosMutableList: MutableList<MealMacrosDataForGet>? = mealMacrosList?.toMutableList()

                    mutableLiveData.value = mealMacrosMutableList
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                } else {
                    Log.i("GEORGIIIIIIIIII", "FAIL")
                }
            }

            override fun onFailure(call: Call<List<MealMacrosDataForGet>>, t: Throwable) {
                Log.i("GEORGIIIIIIIIII", "FAIL")
            }

        })
        return  mutableLiveData

    }

    fun getDailyIntakeSummed() :MutableLiveData<SummedMealMacros> {
        val mutableLiveData :MutableLiveData<SummedMealMacros> = MutableLiveData()
        val dailyIntakeSummedRequestCall = dailyMacroTargetsService.getDailyMacrosIntakeSummed()

        dailyIntakeSummedRequestCall.enqueue(object: Callback<SummedMealMacros>{
            override fun onResponse(
                call: Call<SummedMealMacros>,
                response: Response<SummedMealMacros>
            ) {
                if(response.isSuccessful){
                    val summedMacrosForReturn = response.body()
                    mutableLiveData.value = summedMacrosForReturn
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                } else {
                    Log.i("GEORGIIIIIIIIII", "FAIL")

                }
            }

            override fun onFailure(call: Call<SummedMealMacros>, t: Throwable) {
                Log.i("GEORGIIIIIIIIII", "FAIL")
            }

        })
        return mutableLiveData
    }


    fun getDailyMacroTargets() :MutableLiveData<GetDailyMacroTargetsData> {
        val dailyMacroTargetsRequestCall = dailyMacroTargetsService.getActiveDailyMacroTargets()
        val mutableLiveData :MutableLiveData<GetDailyMacroTargetsData> = MutableLiveData()

        dailyMacroTargetsRequestCall.enqueue(object : Callback<GetDailyMacroTargetsData> {
            override fun onResponse(
                call: Call<GetDailyMacroTargetsData>,
                response: Response<GetDailyMacroTargetsData>
            ) {
                if (response.isSuccessful) {
                    val dailyMacroTargetsData = response.body()
                    mutableLiveData.value = dailyMacroTargetsData
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                } else {
                    Log.i("GEORGIIIIIIIIII", "FAIL")
                }
            }

            override fun onFailure(call: Call<GetDailyMacroTargetsData>, t: Throwable) {
                Log.i("GEORGIIIIIIIIII", "FAIL")
            }

        })
        return mutableLiveData
    }

    fun getSavedMeals() :MutableLiveData<List<SavedMealForGet>> {

        val savedMealsRequestCall = dailyMacroTargetsService.getSavedMeals()
        val mutableLiveData :MutableLiveData<List<SavedMealForGet>> = MutableLiveData()
        savedMealsRequestCall.enqueue(object: Callback<List<SavedMealForGet>>{
            override fun onResponse(
                call: Call<List<SavedMealForGet>>,
                response: Response<List<SavedMealForGet>>
            ) {
                if (response.isSuccessful){
                    val savedMeals = response.body()
                    mutableLiveData.value = savedMeals
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                }
                else {
                    Log.i("GEORGIIIIIIIIII", "FAIL")
                }

            }

            override fun onFailure(call: Call<List<SavedMealForGet>>, t: Throwable) {
                Log.i("GEORGIIIIIIIIII", "FAIL")
            }

        })
        return mutableLiveData

    }

    fun getMealsHistory(month: String) :MutableLiveData<List<MealsHistoryForGet>> {
        val mealsHistoryRequestCall =dailyMacroTargetsService.getMealsHistory(month)
        val mutableLiveData :MutableLiveData<List<MealsHistoryForGet>> = MutableLiveData()

        mealsHistoryRequestCall.enqueue(object : Callback<List<MealsHistoryForGet>>{
            override fun onResponse(
                call: Call<List<MealsHistoryForGet>>,
                response: Response<List<MealsHistoryForGet>>
            ) {
                if(response.isSuccessful){
                    val mealsHistory = response.body()
                    mutableLiveData.value = mealsHistory
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                } else {
                    Log.i("GEORGIIIIIIIIII", "SUCCESS")
                }

            }

            override fun onFailure(call: Call<List<MealsHistoryForGet>>, t: Throwable) {
                Log.i("GEORGIIIIIIIIII", "SUCCESS")
            }

        })

        return mutableLiveData

    }
}