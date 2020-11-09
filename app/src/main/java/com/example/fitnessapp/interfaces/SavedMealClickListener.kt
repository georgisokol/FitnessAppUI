package com.example.fitnessapp.interfaces

import com.example.fitnessapp.dataclasses.SavedMealForGet

interface SavedMealClickListener {
    fun containerClicked(savedMeal : SavedMealForGet)
}