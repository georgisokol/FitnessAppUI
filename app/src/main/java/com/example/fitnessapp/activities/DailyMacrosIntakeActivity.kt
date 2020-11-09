package com.example.fitnessapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.MealMacrosRecyclerAdapter
import com.example.fitnessapp.dataclasses.MealMacrosDataForGet
import com.example.fitnessapp.dataclasses.MealMacrosForPost
import com.example.fitnessapp.dataclasses.SavedMealsForCreation
import com.example.fitnessapp.interfaces.NameDeleteSaveClickListener
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.hideKeyboardFrom
import com.example.fitnessapp.viewmodels.MacrosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_daily_macros_intake.*
import kotlinx.android.synthetic.main.macro_meals_list_item_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DailyMacrosIntakeActivity : AppCompatActivity(), NameDeleteSaveClickListener {
    private var mealMacrosArray: MutableList<MealMacrosDataForGet> = mutableListOf()
    var mealMacrosArrayFromAPI: MutableList<MealMacrosDataForGet> = mutableListOf()
    private lateinit var macrosAdapter: MealMacrosRecyclerAdapter
    private val macrosViewModel: MacrosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_macros_intake)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val emptyMeal = MealMacrosDataForGet(null, "", 0, 0, 0)


        macrosViewModel.getMealMacros()
        macrosViewModel.mealMacrosListLive.observe(this@DailyMacrosIntakeActivity, Observer {
            mealMacrosArrayFromAPI = it
            if (mealMacrosArrayFromAPI.size == 0) {
                mealMacrosArray.add(emptyMeal)
                macrosAdapter = MealMacrosRecyclerAdapter(
                    this@DailyMacrosIntakeActivity,
                    mealMacrosArray,
                    this@DailyMacrosIntakeActivity
                )
                mealsList.adapter = macrosAdapter
            } else {
                macrosAdapter = MealMacrosRecyclerAdapter(
                    this@DailyMacrosIntakeActivity,
                    mealMacrosArrayFromAPI,
                    this@DailyMacrosIntakeActivity
                )
                mealsList.adapter = macrosAdapter
            }


        })

        mealsList.layoutManager = LinearLayoutManager(this)

        mealsList.adapter =
            MealMacrosRecyclerAdapter(this, mealMacrosArray, this as NameDeleteSaveClickListener)

        addMeal.setOnClickListener {

            mealMacrosArrayFromAPI.add(emptyMeal)
            (mealsList.adapter as MealMacrosRecyclerAdapter).notifyDataSetChanged()
        }


    }

    override fun giveNameToMeal(
        input: EditText,
        name: TextView,
        saveButton: ImageButton,
        mealMacros: MealMacrosDataForGet, cancelButton: ImageButton
    ) {
        name.visibility = View.GONE
        input.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE

        input.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        if (name.text != "") {
            input.setText(mealMacros.mealName.toString())
            input.setSelection(input.text.length)
        }

        saveButton.setOnClickListener {
            if (input.text.isNotEmpty()) {
                name.text = input.text.toString()
                name.visibility = View.VISIBLE
                saveButton.visibility = View.GONE
                input.visibility = View.GONE
                hideKeyboardFrom(this, input)
            }
        }
        cancelButton.setOnClickListener {
            name.visibility = View.VISIBLE
            saveButton.visibility = View.GONE
            input.visibility = View.GONE
            cancelButton.visibility = View.GONE
            hideKeyboardFrom(this, input)
        }


    }

    override fun deleteMeal(uId: String?, position: Int) {
        if (uId == null) {
            mealMacrosArray.removeAt(position)
            (mealsList.adapter as MealMacrosRecyclerAdapter).notifyDataSetChanged()
        } else {
            val dailyMacroTargetsService =
                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
            val deleteMealMacrosRequestCall = dailyMacroTargetsService.deleteMealMacros(uId)
            deleteMealMacrosRequestCall.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Snackbar.make(
                            button_addThisMeal,
                            "Sucessfully Deleted",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        macrosViewModel.getMealMacros()
                        macrosViewModel.mealMacrosListLive.observe(
                            this@DailyMacrosIntakeActivity,
                            Observer {
                                mealMacrosArrayFromAPI = it
                                macrosAdapter = MealMacrosRecyclerAdapter(
                                    this@DailyMacrosIntakeActivity,
                                    mealMacrosArrayFromAPI,
                                    this@DailyMacrosIntakeActivity
                                )
                                mealsList.adapter = macrosAdapter


                            })

                    } else {
                        Toast.makeText(
                            this@DailyMacrosIntakeActivity,
                            " Unsuccessfully deleted meal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(
                        this@DailyMacrosIntakeActivity,
                        " Unsuccessfully deleted meal",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    override fun saveMeal(mealName: TextView, protein: EditText, carbs: EditText, fats: EditText) {
        val dailyMacroTargetsService =
            ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
        var macrosAreNotEmptyFlag = false
        var mealNameGivenFlag = false

        if (protein.text.isEmpty() || carbs.text.isEmpty() || fats.text.isEmpty()) {
            Toast.makeText(
                this@DailyMacrosIntakeActivity,
                "you need to fill all the inputs",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            macrosAreNotEmptyFlag = true
        }

        if (mealName.text.isEmpty()) {
            Toast.makeText(
                this@DailyMacrosIntakeActivity,
                "Go to more options and give a name to this meal",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mealNameGivenFlag = true
        }

        if (macrosAreNotEmptyFlag && mealNameGivenFlag) {
            val mealForSave = SavedMealsForCreation(
                protein.text.toString().toInt(),
                carbs.text.toString().toInt(),
                fats.text.toString().toInt(),
                mealName.text.toString()
            )
            val addMealToSavedMealsRequestCall = dailyMacroTargetsService.addMealToSavedMeals(
                mealForSave
            )

            addMealToSavedMealsRequestCall.enqueue(object : Callback<SavedMealsForCreation> {
                override fun onResponse(
                    call: Call<SavedMealsForCreation>,
                    response: Response<SavedMealsForCreation>
                ) {
                    if (response.isSuccessful) {
                        Snackbar.make(
                            addMeal,
                            "Successfully Added the Meal to Saved Meals",
                            Snackbar.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            this@DailyMacrosIntakeActivity,
                            " Unsuccessfully added meal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SavedMealsForCreation>, t: Throwable) {
                    Toast.makeText(
                        this@DailyMacrosIntakeActivity,
                        " Unsuccessfully added meal",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }


    }

    override fun addAndPostMeal(
        mealName: String,
        proteins: String,
        carbs: String,
        fats: String,
        uId: String?, mealProteinInput: EditText, mealCarbsInput: EditText, mealFatsInput: EditText
    ) {
        var canPostOrUpdateFlag = false
        val mealMacrosForPostObject: MealMacrosForPost

        if (proteins.isEmpty() || carbs.isEmpty() || fats.isEmpty()) {
            Toast.makeText(
                this@DailyMacrosIntakeActivity,
                "you need to fill all the inputs",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            canPostOrUpdateFlag = true
        }


        val dailyMacroTargetsService =
            ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
        if (canPostOrUpdateFlag) {


            mealMacrosForPostObject = if (mealName.isEmpty()) {
                MealMacrosForPost("", proteins.toInt(), carbs.toInt(), fats.toInt())
            } else {
                MealMacrosForPost(mealName, proteins.toInt(), carbs.toInt(), fats.toInt())
            }
            // POST CALL

            if (uId == null) {
                // POST CALL

                val addMealMacroRequestCall =
                    dailyMacroTargetsService.addMealMacros(mealMacrosForPostObject)

                addMealMacroRequestCall.enqueue(object : Callback<MealMacrosForPost> {
                    override fun onResponse(
                        call: Call<MealMacrosForPost>,
                        response: Response<MealMacrosForPost>
                    ) {
                        if (response.isSuccessful) {
                            Snackbar.make(addMeal, "Succefully Added a Meal", Snackbar.LENGTH_SHORT)
                                .show()
                            macrosViewModel.getMealMacros()
                            macrosViewModel.mealMacrosListLive.observe(
                                this@DailyMacrosIntakeActivity,
                                Observer {
                                    mealMacrosArrayFromAPI = it
                                    macrosAdapter = MealMacrosRecyclerAdapter(
                                        this@DailyMacrosIntakeActivity,
                                        mealMacrosArrayFromAPI,
                                        this@DailyMacrosIntakeActivity
                                    )
                                    mealsList.adapter = macrosAdapter


                                })

                        } else {
                            Toast.makeText(
                                this@DailyMacrosIntakeActivity,
                                " Unsuccessfully added meal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    override fun onFailure(call: Call<MealMacrosForPost>, t: Throwable) {
                        Toast.makeText(
                            this@DailyMacrosIntakeActivity,
                            " Unsuccessfully added meal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            } else {
                //PUT CALL

                val updateMealMacroRequestCall = dailyMacroTargetsService.updateMealMacros(
                    uId,
                    mealMacrosForPostObject
                )
                updateMealMacroRequestCall.enqueue(object : Callback<MealMacrosForPost> {
                    override fun onResponse(
                        call: Call<MealMacrosForPost>,
                        response: Response<MealMacrosForPost>
                    ) {
                        if (response.isSuccessful) {
                            Snackbar.make(
                                addMeal,
                                "Succefully Updated a Meal",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            macrosViewModel.getMealMacros()
                            macrosViewModel.mealMacrosListLive.observe(
                                this@DailyMacrosIntakeActivity,
                                Observer {
                                    mealMacrosArrayFromAPI = it
                                    macrosAdapter = MealMacrosRecyclerAdapter(
                                        this@DailyMacrosIntakeActivity,
                                        mealMacrosArrayFromAPI,
                                        this@DailyMacrosIntakeActivity
                                    )
                                    mealsList.adapter = macrosAdapter


                                })

                        } else {
                            Toast.makeText(
                                this@DailyMacrosIntakeActivity,
                                " Unsuccessfully updated meal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<MealMacrosForPost>, t: Throwable) {
                        Toast.makeText(
                            this@DailyMacrosIntakeActivity,
                            " Unsuccessfully updated meal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


            }
            hideSoftKeyboard(this@DailyMacrosIntakeActivity, addMeal)
            mealProteinInput.clearFocus()
            mealCarbsInput.clearFocus()
            mealFatsInput.clearFocus()


        }


    }

    private fun hideSoftKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_daily_intake_favorites, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_favorites -> {
                val activityIntent = Intent(
                    this@DailyMacrosIntakeActivity,
                    FavoriteMealsActivity::class.java
                )
                startActivity(activityIntent)
                finish()
            }
        }
        return true
    }
}