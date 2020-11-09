package com.example.fitnessapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.viewmodels.MacrosViewModel
import kotlinx.android.synthetic.main.activity_daily_macros_intake.toolbar
import kotlinx.android.synthetic.main.activity_meals_history.*
import java.text.SimpleDateFormat
import java.util.*

class MealsHistoryActivity : AppCompatActivity() {
    private val mealsHistoryViewModel: MacrosViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val dateFormatText = SimpleDateFormat("MMMM")
        val dateFormatNumber = SimpleDateFormat("MM")
        val date = Date()
        val dateNumber = dateFormatNumber.format(date)
        text_month.text = dateFormatText.format(date)


        mealsHistoryViewModel.getMealsHistory(dateNumber)
        mealsHistoryViewModel.mealsHistoryListLive.observe(this, Observer{
            val mealsHistoryList = it
            val tableRow = LayoutInflater.from(this).inflate(R.layout.history_table_item_layout, null , false)
            val tableRowDate:TextView = tableRow.findViewById(R.id.date)
            val tableRowCalories:TextView = tableRow.findViewById(R.id.calories)
            val tableRowProtein:TextView = tableRow.findViewById(R.id.protein)
            val tableRowCarbs:TextView = tableRow.findViewById(R.id.carbs)
            val tableRowFats:TextView = tableRow.findViewById(R.id.fats)

            if(mealsHistoryList != null) {
                for(meal in mealsHistoryList) {
                    val calories = (meal.protein *4) + (meal.carbs *4) + (meal.fats * 9)
                    val date = meal.creationDate.substring(8,10)
                    var dateText:String
                    dateText = if(date == "01") {
                        "1st"
                    } else if(date == "02") {
                        "2nd"
                    } else if(date == "03") {
                        "3rd"
                    } else {
                        if(date.toInt()<10) {
                            date.substring(1,2) + "th"
                        } else{
                            (date + "th")
                        }

                    }

                    tableRowDate.text = dateText
                    tableRowCalories.text = calories.toString()
                    tableRowProtein.text = meal.protein.toString()
                    tableRowCarbs.text = meal.carbs.toString()
                    tableRowFats.text = meal.fats.toString()
                    historyTable.addView(tableRow)
                }
            }





        })




    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}