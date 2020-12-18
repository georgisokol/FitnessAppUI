package com.example.fitnessapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.SavedMealRecyclerAdapter
import com.example.fitnessapp.dataclasses.MealMacrosDataForGet
import com.example.fitnessapp.dataclasses.MealMacrosForPost
import com.example.fitnessapp.dataclasses.SavedMealForGet
import com.example.fitnessapp.fragments.ServerOrConnectivityErrorDialog
import com.example.fitnessapp.interfaces.SavedMealClickListener
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.MacrosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_daily_macros_intake.toolbar
import kotlinx.android.synthetic.main.activity_favorite_meals.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteMealsActivity : AppCompatActivity(), SavedMealClickListener , ServerAndConnectivityInterface{
    private val savedMealsViewModel: MacrosViewModel by viewModels()
    private lateinit var savedMealsAdapter: SavedMealRecyclerAdapter
    lateinit var myPreference: MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_meals)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        savedMealsList.layoutManager = LinearLayoutManager(this)
        myPreference = MyPreference(this)


        savedMealsViewModel.getSavedMeals(myPreference.getUserUid()!!)
        savedMealsViewModel.serverOrConnectivityInterface = this



        savedMealsViewModel.savedMealsListLive.observe(this@FavoriteMealsActivity, Observer {
            val savedMealArray = it
            if (savedMealArray.isNotEmpty()) {
                savedMealsAdapter = SavedMealRecyclerAdapter(
                    this,
                    savedMealArray,
                    this as SavedMealClickListener
                )
                savedMealsList.adapter = savedMealsAdapter
            }


        })


    }


    override fun containerClicked(savedMeal: SavedMealForGet) {
        val dailyMacroTargetsService =
            ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

        val mealMacrosForPost = MealMacrosForPost(
            savedMeal.mealName,
            savedMeal.protein,
            savedMeal.carbs,
            savedMeal.fats
        )

        val addMealMacrosRequestCall =
            dailyMacroTargetsService.addMealMacros(myPreference.getUserUid()!!, mealMacrosForPost)

        addMealMacrosRequestCall.enqueue(object : Callback<MealMacrosDataForGet> {
            override fun onResponse(
                call: Call<MealMacrosDataForGet>,
                response: Response<MealMacrosDataForGet>
            ) {
                if (response.isSuccessful) {
                    Snackbar.make(
                        savedMealsList,
                        "Successfully Added the meal: " + savedMeal.mealName,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    val activityIntent = Intent(
                        this@FavoriteMealsActivity,
                        DailyMacrosIntakeActivity::class.java
                    )
                    startActivity(activityIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this@FavoriteMealsActivity,
                        " Unsuccessfully added meal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MealMacrosDataForGet>, t: Throwable) {
                Toast.makeText(
                    this@FavoriteMealsActivity,
                    " Unsuccessfully added meal",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val activityIntent = Intent(
                    this@FavoriteMealsActivity,
                    DailyMacrosIntakeActivity::class.java
                )
                startActivity(activityIntent)
                finish()
            }
        }
        return true
    }

    override fun onServerError() {
        val errorMessage = "Could not connect to server"
        val serverOrConnectivityErrorDialog = ServerOrConnectivityErrorDialog().newInstance(errorMessage)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag("connectionErrorDialog")
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)
        serverOrConnectivityErrorDialog.show(fragmentTransaction, "connectionErrorDialog")
    }

    override fun onConnectivityError() {
        val errorMessage = "Check your internet connection"
        val serverOrConnectivityErrorDialog = ServerOrConnectivityErrorDialog().newInstance(errorMessage)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag("connectionErrorDialog")
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)
        serverOrConnectivityErrorDialog.show(fragmentTransaction, "connectionErrorDialog")
    }
}