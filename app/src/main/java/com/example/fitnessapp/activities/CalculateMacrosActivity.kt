package com.example.fitnessapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.MenuItem
import android.widget.Toast
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.DailyMacroTargetsData
import com.example.fitnessapp.dataclasses.GetDailyMacroTargetsData
import com.example.fitnessapp.dataclasses.UserDataForCreation
import com.example.fitnessapp.dataclasses.UserDataForGet
import com.example.fitnessapp.fragments.ExerciseFrequencyBottomSheetDialog
import com.example.fitnessapp.fragments.ExerciseGoalBottomSheetDialog
import com.example.fitnessapp.fragments.ExerciseTypeBottomSheetDialog
import com.example.fitnessapp.fragments.GenderBottomSheetDialog
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import kotlinx.android.synthetic.main.activity_calculate_macros_first_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class CalculateMacrosActivity : AppCompatActivity(),
    ExerciseFrequencyBottomSheetDialog.ExerciseFrequencyBottomSheetListener,
    GenderBottomSheetDialog.GenderBottomSheetListener,
    ExerciseTypeBottomSheetDialog.ExerciseTypeBottomSheetListener,
    ExerciseGoalBottomSheetDialog.ExerciseGoalBottomSheetListener {
    lateinit var myPreference: MyPreference
     var dailyMacroTargetsUid: String? = null
     var userUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_macros_first_page)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_cancel_24)

        myPreference = MyPreference(this)

        if (myPreference.getFirstTimeOpeningApp() != null) {
            val dailyMacroTargetsService =
                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

            // GET User

            val userRequestCall = dailyMacroTargetsService.getUser()

            userRequestCall.enqueue(object :Callback<UserDataForGet>{
                override fun onResponse(
                    call: Call<UserDataForGet>,
                    response: Response<UserDataForGet>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "USER DATA RETRIEVED",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val user = response.body()
                        if(user != null ){
                            userUid = user.Uid
                            fillUserInformation(user)
                        } else {
                            userUid = null
                        }



                    } else {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "USER DATA FAILED TO BE RETRIEVED",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<UserDataForGet>, t: Throwable) {
                    Toast.makeText(
                        this@CalculateMacrosActivity,
                        "USER DATA FAILED TO BE RETRIEVED",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })

            // GET Macros
            val requestCallDailyMacroTargets = dailyMacroTargetsService.getActiveDailyMacroTargets()

            requestCallDailyMacroTargets.enqueue(object : Callback<GetDailyMacroTargetsData> {
                override fun onResponse(
                    call: Call<GetDailyMacroTargetsData>,
                    response: Response<GetDailyMacroTargetsData>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Gi ZEMAV PODATOCITE ZA MACROS - OT",
                            Toast.LENGTH_SHORT
                        ).show()
                        val dailyMacroTargetsData = response.body()
                        dailyMacroTargetsUid = if(dailyMacroTargetsData != null) {
                            dailyMacroTargetsData.Uid
                        } else {
                            null
                        }



                    }
                }

                override fun onFailure(call: Call<GetDailyMacroTargetsData>, t: Throwable) {
                    Toast.makeText(
                        this@CalculateMacrosActivity,
                        "FAIL ZA ZIMANJE PODATOCI ZA MACROS - OT",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })




        }



        text_iExercise.setOnClickListener {
            val exerciseFrequencyBottomSheetDialog = ExerciseFrequencyBottomSheetDialog()
            exerciseFrequencyBottomSheetDialog.show(
                supportFragmentManager,
                "exerciseFrequencyBottomSheet"
            )
        }
        text_gender.setOnClickListener {
            val genderBottomSheetDialog = GenderBottomSheetDialog()
            genderBottomSheetDialog.show(supportFragmentManager, "genderBottomSheet")
        }
        text_forExercise.setOnClickListener {
            val exerciseTypeBottomSheetDialog = ExerciseTypeBottomSheetDialog()
            exerciseTypeBottomSheetDialog.show(supportFragmentManager, "exerciseTypeBottomSheet")
        }
        text_iWantTo.setOnClickListener {
            val exerciseGoalBottomSheetDialog = ExerciseGoalBottomSheetDialog()
            exerciseGoalBottomSheetDialog.show(supportFragmentManager, "exerciseGoalBottomSheet")
        }
        letsGoContainer.setOnClickListener {
            addOrUpdateDailyMacroTargetsOrUsers()


        }


    }

    private fun makeUserObjectForCreation() :UserDataForCreation {
        val age = edit_age.text.toString().toInt()
        val gender = when (text_gender.text.toString()) {
            resources.getString(R.string.male) -> 0
            else -> 1
        }
        val height = edit_Height.text.toString().toInt()
        val weight = edit_Weight.text.toString().toInt()
        val frequency = when (text_iExercise.text.toString()) {
            resources.getString(R.string.exercise_frequency_rarely) -> 0
            resources.getString(R.string.exercise_frequency_1_3) ->1
            resources.getString(R.string.exercise_frequency_3_5) ->2
            else -> 3
        }
        val type = when(text_forExercise.text.toString()) {
            resources.getString(R.string.walk_sometimes) -> 0
            resources.getString(R.string.i_do_cardio) -> 1
            else -> 2
        }
        val goal = when(text_iWantTo.text.toString()) {
            resources.getString(R.string.lose_some_fat) -> 0
            resources.getString(R.string.build_some_muscle) -> 1
            else -> 2
        }

        return UserDataForCreation(age, gender, height, weight, frequency , type, goal)

    }

    fun fillUserInformation(user: UserDataForGet) {
        edit_age.setText(user.age.toString())
        when (user.gender) {
            0 -> text_gender.text = resources.getString(R.string.male)
            1 -> text_gender.text = resources.getString(R.string.female)
        }
        edit_Height.setText(user.height.toString())
        edit_Weight.setText(user.weight.toString())

        when (user.exerciseFrequency) {
            0 -> text_iExercise.text = resources.getString(R.string.exercise_frequency_rarely)
            1 -> text_iExercise.text = resources.getString(R.string.exercise_frequency_1_3)
            2 -> text_iExercise.text = resources.getString(R.string.exercise_frequency_3_5)
            3 -> text_iExercise.text = resources.getString(R.string.exercise_frequency_6_7)
        }
        when (user.exerciseType) {
            0 -> text_forExercise.text = resources.getString(R.string.walk_sometimes)
            1 -> text_forExercise.text = resources.getString(R.string.i_do_cardio)
            2 -> text_forExercise.text = resources.getString(R.string.i_lift_weights)
        }
        when (user.exerciseGoal) {
            0 -> text_iWantTo.text = resources.getString(R.string.lose_some_fat)
            1 -> text_iWantTo.text = resources.getString(R.string.build_some_muscle)
            2 -> text_iWantTo.text = resources.getString(R.string.stay_the_same)
        }
    }


    private fun addOrUpdateDailyMacroTargetsOrUsers() {

        if (myPreference.getFirstTimeOpeningApp() == null) {
            val dailyMacroTargetsService =
                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

            // DAILY MACRO TARGETS POST
            val dailyMacroTargetsRequestCall = dailyMacroTargetsService.addActiveDailyMacroTargets(
                calculateTheMacros()
            )

            dailyMacroTargetsRequestCall.enqueue(object : Callback<DailyMacroTargetsData> {
                override fun onFailure(
                    call: Call<DailyMacroTargetsData>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@CalculateMacrosActivity,
                        "Failed to add item!!!!!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                override fun onResponse(
                    call: Call<DailyMacroTargetsData>,
                    response: Response<DailyMacroTargetsData>
                ) {
                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Successfully Added",
                            Toast.LENGTH_LONG
                        ).show()

                        myPreference.setFirstTimeOpeningApp("notFirst")
                        val activityIntent =
                            Intent(this@CalculateMacrosActivity, HomeActivity::class.java)
                        startActivity(activityIntent)
                    } else {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Failed to add item!!!!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })


            // USER POST

            val userRequestCall = dailyMacroTargetsService.addUser(makeUserObjectForCreation())
            userRequestCall.enqueue(object: Callback<UserDataForCreation>{
                override fun onResponse(
                    call: Call<UserDataForCreation>,
                    response: Response<UserDataForCreation>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Successfully Added User",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Failed to add User",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onFailure(call: Call<UserDataForCreation>, t: Throwable) {
                    Toast.makeText(
                        this@CalculateMacrosActivity,
                        "Failed to Add User",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })




        } else {
            val dailyMacroTargetsService =
                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

            // Daily Macro Targets Put


            if(dailyMacroTargetsUid != null ) {
                val dailyMacroTargetsRequestCall =
                    dailyMacroTargetsService.updateActiveDailyMacroTargets(dailyMacroTargetsUid!!, calculateTheMacros())
                dailyMacroTargetsRequestCall.enqueue(object : Callback<DailyMacroTargetsData> {
                    override fun onResponse(
                        call: Call<DailyMacroTargetsData>,
                        response: Response<DailyMacroTargetsData>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CalculateMacrosActivity,
                                "Successfully Updated DailyMacroTargets",
                                Toast.LENGTH_LONG
                            ).show()
                            val activityIntent =
                                Intent(this@CalculateMacrosActivity, HomeActivity::class.java)
                            startActivity(activityIntent)
                        } else {
                            Toast.makeText(
                                this@CalculateMacrosActivity,
                                "UnSuccessfully Updated DailyMacroTargets",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<DailyMacroTargetsData>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "Failed to  Update",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            } else {
                Toast.makeText(this@CalculateMacrosActivity, "Connect To Internet so u can change ur data", Toast.LENGTH_SHORT).show()
            }


            // User Put

            if(userUid != null) {
                val userRequestCall = dailyMacroTargetsService.updateUser(userUid!!, makeUserObjectForCreation())
                userRequestCall.enqueue(object :Callback<UserDataForCreation>{
                    override fun onResponse(
                        call: Call<UserDataForCreation>,
                        response: Response<UserDataForCreation>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CalculateMacrosActivity,
                                "Successfully Updated User",
                                Toast.LENGTH_LONG
                            ).show()
                            val activityIntent =
                                Intent(this@CalculateMacrosActivity, HomeActivity::class.java)
                            startActivity(activityIntent)
                        } else {
                            Toast.makeText(
                                this@CalculateMacrosActivity,
                                "UnSuccessfully Updated User",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserDataForCreation>, t: Throwable) {
                        Toast.makeText(
                            this@CalculateMacrosActivity,
                            "UnSuccessfully Updated User",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            } else {
                Toast.makeText(this@CalculateMacrosActivity, "Connect To Internet so u can change ur data", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun calculateTheMacros(): DailyMacroTargetsData {
        val BMR: Int = if (text_gender.text.toString() == resources.getString(R.string.male)) {
            (66.47 + (13.75 * edit_Weight.text.toString()
                .toInt()) + (5.003 * edit_Height.text.toString()
                .toInt()) - (6.755 * edit_age.text.toString().toInt())).roundToInt()
        } else {
            (655.1 + (9.563 * edit_Weight.text.toString()
                .toInt()) + (1.85 * edit_Height.text.toString()
                .toInt()) - (4.676 * edit_age.text.toString().toInt())).roundToInt()
        }


        val BMRWithActivity: Int
        BMRWithActivity = when {
            text_iExercise.text.toString() == resources.getString(R.string.exercise_frequency_rarely) -> {
                (BMR * 1.2).roundToInt()
            }
            text_iExercise.text.toString() == resources.getString(R.string.exercise_frequency_1_3) -> {
                (BMR * 1.375).roundToInt()
            }
            text_iExercise.text.toString() == resources.getString(R.string.exercise_frequency_3_5) -> {
                (BMR * 1.55).roundToInt()
            }
            else -> {
                (BMR * 1.725).roundToInt()
            }
        }

        val neededCalories: Int
        neededCalories = when {
            text_iWantTo.text.toString() == resources.getString(R.string.build_some_muscle) -> {
                BMRWithActivity + 250
            }
            text_iWantTo.text.toString() == resources.getString(R.string.lose_some_fat) -> {
                (BMRWithActivity * 0.85).roundToInt()
            }
            else -> {
                BMRWithActivity
            }

        }

        val trainingProtein = (neededCalories * 0.32 / 4).roundToInt()
        val trainingCarbs = (neededCalories * 0.48 / 4).roundToInt()
        val trainingFats = (neededCalories * 0.2 / 9).roundToInt()
        val restProtein = (neededCalories * 0.32 / 4).roundToInt()
        val restCarbs = (neededCalories * 0.28 / 4).roundToInt()
        val restFats = (neededCalories * 0.4 / 9).roundToInt()




        return DailyMacroTargetsData(
            trainingProtein,
            trainingCarbs,
            trainingFats,
            restProtein,
            restCarbs,
            restFats,
            false
        )


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFrequencyOptionClicked(frequency: String) {
        text_iExercise.text = frequency
    }

    override fun genderOptionClicked(gender: String) {
        text_gender.text = gender
    }

    override fun typeOptionClicked(type: String) {
        text_forExercise.text = type
    }

    override fun goalOptionClicked(goal: String) {
        text_iWantTo.text = goal
    }
}