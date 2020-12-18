package com.example.fitnessapp.interfaces

interface HomeActivityUIInterface {
    fun notFirstTimeOpeningAndRestAndTrainingAlreadySet()
    fun firstTimeOpening()
    fun notFirstTimeOpeningAndRestAndTrainingNotSet()
    fun notFirstTImeOpeningAndSuggestRestDay()
    fun onFailure()

}