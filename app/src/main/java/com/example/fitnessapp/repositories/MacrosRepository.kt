package com.example.fitnessapp.repositories

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.dataclasses.*
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.network.NoConnectivityException
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.coroutineContext


class MacrosRepository(application :Application) {
    val dailyMacroTargetsService  =
        ServiceBuilder.buildService(DailyMacroTargetsService::class.java)



    fun getMealMacros(userUid: String) :MutableLiveData<MutableList<MealMacrosDataForGet>> {
        val mutableLiveData : MutableLiveData<MutableList<MealMacrosDataForGet>> = MutableLiveData()


            val mealMacrosRequestCall = dailyMacroTargetsService.getListOfDailyMealMacros(userUid)
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

    suspend fun getDailyIntakeSummed(userUid: String) :Response<SummedMealMacros> {
            return dailyMacroTargetsService.getDailyMacrosIntakeSummed(userUid)
    }




  suspend fun getDailyMacroTargets(userUid:String) :Response<GetDailyMacroTargetsData> {
      return dailyMacroTargetsService.getActiveDailyMacroTargets(userUid)
    }

    fun getSavedMeals(userUid: String) :MutableLiveData<List<SavedMealForGet>> {
        val mutableLiveData :MutableLiveData<List<SavedMealForGet>> = MutableLiveData()

            val savedMealsRequestCall = dailyMacroTargetsService.getSavedMeals(userUid)

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

    suspend fun getMealsHistory(userUid: String,month: String) :Response<List<MealsHistoryForGet>> {
        return dailyMacroTargetsService.getMealsHistory(userUid, month)

    }
    suspend fun updateTrainingDay(userUid: String, uId: String, isTrainingDay: Boolean) :Response<GetDailyMacroTargetsData>{
        return dailyMacroTargetsService.updateTrainingDay(userUid,uId,isTrainingDay)
    }
}