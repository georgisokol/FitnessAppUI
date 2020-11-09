package com.example.fitnessapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.SavedMealRecyclerAdapter
import com.example.fitnessapp.dataclasses.MealMacrosForPost
import com.example.fitnessapp.dataclasses.SavedMealForGet
import com.example.fitnessapp.interfaces.SavedMealClickListener
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.viewmodels.MacrosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_daily_macros_intake.toolbar
import kotlinx.android.synthetic.main.activity_favorite_meals.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteMealsActivity : AppCompatActivity(),SavedMealClickListener {
    private val savedMealsViewModel: MacrosViewModel by viewModels()
    private lateinit var savedMealsAdapter :SavedMealRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_meals)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        savedMealsList.layoutManager = LinearLayoutManager(this)


        savedMealsViewModel.getSavedMeals()




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
        val dailyMacroTargetsService = ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

        val mealMacrosForPost = MealMacrosForPost(
            savedMeal.mealName,
            savedMeal.protein,
            savedMeal.carbs,
            savedMeal.fats
        )

        val addMealMacrosRequestCall = dailyMacroTargetsService.addMealMacros(mealMacrosForPost)

        addMealMacrosRequestCall.enqueue(object : Callback<MealMacrosForPost> {
            override fun onResponse(
                call: Call<MealMacrosForPost>,
                response: Response<MealMacrosForPost>
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

            override fun onFailure(call: Call<MealMacrosForPost>, t: Throwable) {
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
}