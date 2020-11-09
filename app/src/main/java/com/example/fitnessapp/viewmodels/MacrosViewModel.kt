package com.example.fitnessapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.dataclasses.*
import com.example.fitnessapp.repositories.MacrosRepository

class MacrosViewModel(application: Application) : AndroidViewModel(application) {
    private var macrosRepository: MacrosRepository = MacrosRepository(application)


    var mealMacrosListLive = MutableLiveData<MutableList<MealMacrosDataForGet>>()

    var dailyMacroIntakeLive = MutableLiveData<SummedMealMacros>()

    var dailyMacroTargetsLive = MutableLiveData<GetDailyMacroTargetsData>()

    var savedMealsListLive = MutableLiveData<List<SavedMealForGet>>()

    var mealsHistoryListLive = MutableLiveData<List<MealsHistoryForGet>>()

    fun getMealMacros() :LiveData<MutableList<MealMacrosDataForGet>> {
        mealMacrosListLive = macrosRepository.getMealMacros()
        return mealMacrosListLive
    }

    fun getDailyMacroIntake() :MutableLiveData<SummedMealMacros> {
        dailyMacroIntakeLive = macrosRepository.getDailyIntakeSummed()
        return dailyMacroIntakeLive

    }

    fun getDailyMacroTargets() :MutableLiveData<GetDailyMacroTargetsData> {
        dailyMacroTargetsLive = macrosRepository.getDailyMacroTargets()
        return dailyMacroTargetsLive
    }

    fun getSavedMeals(): MutableLiveData<List<SavedMealForGet>> {
        savedMealsListLive = macrosRepository.getSavedMeals()
        return savedMealsListLive
    }

    fun getMealsHistory(month: String) :MutableLiveData<List<MealsHistoryForGet>> {
        mealsHistoryListLive = macrosRepository.getMealsHistory(month)
        return mealsHistoryListLive
    }




}