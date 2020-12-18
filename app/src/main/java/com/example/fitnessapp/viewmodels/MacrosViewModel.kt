package com.example.fitnessapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.dataclasses.*
import com.example.fitnessapp.fragments.RestDaySuggestionDialogFragment
import com.example.fitnessapp.interfaces.HomeActivityUIInterface
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.network.NoConnectivityException
import com.example.fitnessapp.repositories.MacrosRepository
import com.example.fitnessapp.utils.MyPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class MacrosViewModel(application: Application) : AndroidViewModel(application) {
    private var macrosRepository: MacrosRepository = MacrosRepository(application)

    var myPreference = MyPreference(application.applicationContext)

    var homeActivityUIListener :HomeActivityUIInterface? = null
    var serverOrConnectivityInterface : ServerAndConnectivityInterface? = null


    var mealMacrosListLive = MutableLiveData<MutableList<MealMacrosDataForGet>>()

    var dailyIntakeSummedLive = MutableLiveData<SummedMealMacros>()



    var dailyMacroTargetsLive: MutableLiveData<GetDailyMacroTargetsData>? =
        MutableLiveData<GetDailyMacroTargetsData>()

    var savedMealsListLive = MutableLiveData<List<SavedMealForGet>>()

    var mealsHistoryListLive :MutableLiveData<List<MealsHistoryForGet>>? = MutableLiveData<List<MealsHistoryForGet>>()

    fun getMealMacros(userUid: String): LiveData<MutableList<MealMacrosDataForGet>> {
        mealMacrosListLive = macrosRepository.getMealMacros(userUid)
        return mealMacrosListLive
    }

    fun getDailyIntakeSummed(userUid: String): MutableLiveData<SummedMealMacros> {
        viewModelScope.launch {
            try {
                val response = macrosRepository.getDailyIntakeSummed(userUid)
                if(response.isSuccessful) {
                    dailyIntakeSummedLive.value = response.body()!!
                } else {
                    dailyIntakeSummedLive.value = SummedMealMacros(0,0,0)
                }
            }catch (e: NoConnectivityException) {
                Log.e("Connectivity", e.message)
                serverOrConnectivityInterface?.onConnectivityError()
            } catch( e: SocketTimeoutException ) {
                Log.e("Server status", "SERVER IS DOWN")
                serverOrConnectivityInterface?.onServerError()
            }

        }

        return dailyIntakeSummedLive

    }


    fun getDailyMacroTargets(userUid: String): MutableLiveData<GetDailyMacroTargetsData>? {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                if(!myPreference.getRestDaySuggestionAlreadyPopped()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val dateFormatNumber = SimpleDateFormat("MM")
                        val date = Date()
                        val dateNumber = dateFormatNumber.format(date)
                        val response = macrosRepository.getMealsHistory(myPreference.getUserUid()!!, dateNumber)
                        if(response.isSuccessful) {
                            val historyList =  response.body()!!
                            if(historyList.size >= 2) {
                                val dayBeforeYesterday = historyList[historyList.size-2]
                                val yesterday = historyList[historyList.size-1]

                                if(dayBeforeYesterday.isTrainingDay && yesterday.isTrainingDay) {
                                    myPreference.setRestDaySuggestion(true)
                                    myPreference.setRestDaySuggestionAlreadyPopped(true)
                                }
                            }
                        }
                    }
                }

                val response = macrosRepository.getDailyMacroTargets(userUid)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    dailyMacroTargetsLive?.postValue(response.body())
                    if(responseData?.isFirstTime != null && myPreference.getTrainingOrRestDay() != null) {
                        homeActivityUIListener?.notFirstTimeOpeningAndRestAndTrainingAlreadySet()
                    } else if(responseData?.isFirstTime != null && myPreference.getTrainingOrRestDay() == null && !myPreference.getRestDaySuggestion()) {
                        homeActivityUIListener?.notFirstTimeOpeningAndRestAndTrainingNotSet()
                        myPreference.setDailyMacroTargetsUid(responseData.Uid)
                    } else if(responseData?.isFirstTime == null) {
                        homeActivityUIListener?.firstTimeOpening()
                    } else if(responseData.isFirstTime != null && myPreference.getTrainingOrRestDay() == null && myPreference.getRestDaySuggestion()) {
                            homeActivityUIListener?.notFirstTImeOpeningAndSuggestRestDay()

                    }
                } else {
                    dailyMacroTargetsLive = null
                    homeActivityUIListener?.firstTimeOpening()
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", e.message)
                serverOrConnectivityInterface?.onConnectivityError()
            } catch( e: SocketTimeoutException) {
                Log.e("Server status", "SERVER IS DOWN")
                serverOrConnectivityInterface?.onServerError()
            }

        }

        return dailyMacroTargetsLive
    }

    fun getSavedMeals(userUid: String): MutableLiveData<List<SavedMealForGet>> {
        savedMealsListLive = macrosRepository.getSavedMeals(userUid)
        return savedMealsListLive
    }

    fun getMealsHistory(userUid: String, month: String): MutableLiveData<List<MealsHistoryForGet>>? {

            viewModelScope.launch {
                try {
                    val response = macrosRepository.getMealsHistory(userUid,month)
                    if(response.isSuccessful) {
                        mealsHistoryListLive?.value = response.body()
                    } else {
                        mealsHistoryListLive = null
                    }
                } catch (e: NoConnectivityException) {
                    Log.e("Connectivity", e.message)
                    serverOrConnectivityInterface?.onConnectivityError()
                } catch( e: SocketTimeoutException ) {
                    Log.e("Server status", "SERVER IS DOWN")
                    serverOrConnectivityInterface?.onServerError()
                }
            }
        return mealsHistoryListLive
    }

    fun updateTrainingDay(userUid: String, uId:String, isTrainingDay : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = macrosRepository.updateTrainingDay(userUid,uId,isTrainingDay)
                if(response.isSuccessful) {
                    val dailyTargets = response.body()
                    Log.i("Updejtiran", dailyTargets?.isTrainingDay.toString())
                } else {
                    Log.i("FAIL", "NE USPEAAAAA")
                }
            } catch (e: NoConnectivityException) {
                Log.e("Connectivity", e.message)
                serverOrConnectivityInterface?.onConnectivityError()
            } catch( e: SocketTimeoutException ) {
                Log.e("Server status", "SERVER IS DOWN")
                serverOrConnectivityInterface?.onServerError()
            }
        }
    }


}