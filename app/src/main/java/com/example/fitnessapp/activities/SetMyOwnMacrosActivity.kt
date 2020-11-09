package com.example.fitnessapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.DailyMacroTargetsData
import com.example.fitnessapp.dataclasses.GetDailyMacroTargetsData
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_set_my_own_macros.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SetMyOwnMacrosActivity : AppCompatActivity() {
    lateinit var myPreference: MyPreference
     var Uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_own_macros)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_cancel_24)

        myPreference = MyPreference(this)


        if (myPreference.getFirstTimeOpeningApp() != null) {
            val dailyMacroTargetsService =
                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
            val requestCall = dailyMacroTargetsService.getActiveDailyMacroTargets()

            requestCall.enqueue(object : Callback<GetDailyMacroTargetsData> {
                override fun onResponse(
                    call: Call<GetDailyMacroTargetsData>,
                    response: Response<GetDailyMacroTargetsData>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SetMyOwnMacrosActivity,
                            "SUCCESSFULLY DATA RETRIEVED",
                            Toast.LENGTH_SHORT
                        ).show()

                        val dailyMacroTargetsData = response.body()
                        if(dailyMacroTargetsData != null) {
                            edit_trainingProteins.setText(dailyMacroTargetsData.tProtein.toString())
                            edit_trainingCarbs.setText(dailyMacroTargetsData.tCarbs.toString())
                            edit_trainingFats.setText(dailyMacroTargetsData.tFats.toString())
                            edit_restProteins.setText(dailyMacroTargetsData.rProtein.toString())
                            edit_restCarbs.setText(dailyMacroTargetsData.rCarbs.toString())
                            edit_restFats.setText(dailyMacroTargetsData.rFats.toString())
                            Uid = dailyMacroTargetsData.Uid
                        } else {
                            Uid = null
                        }



                    }
                }

                override fun onFailure(call: Call<GetDailyMacroTargetsData>, t: Throwable) {
                    Toast.makeText(
                        this@SetMyOwnMacrosActivity,
                        "Failed to get data",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_done -> {
                var canPostTheMacroTargetsObjectFlag = false
                if ((edit_trainingProteins.text.toString() == "") || (edit_trainingCarbs.text.toString() == "") || (edit_trainingFats.text.toString() == "") || (edit_restProteins.text.toString() == "") || (edit_restCarbs.text.toString() == "") || (edit_restFats.text.toString() == "")) {
                    Snackbar.make(
                        edit_trainingProteins,
                        "You need to input all the values to continue",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    canPostTheMacroTargetsObjectFlag = false
                } else {
                    canPostTheMacroTargetsObjectFlag = true
                }


                if (canPostTheMacroTargetsObjectFlag) {
                    val newDailyMacroTargets = DailyMacroTargetsData(
                        edit_trainingProteins.text.toString().toInt(),
                        edit_trainingCarbs.text.toString().toInt(),
                        edit_trainingFats.text.toString().toInt(),
                        edit_restProteins.text.toString().toInt(),
                        edit_trainingCarbs.text.toString().toInt(),
                        edit_restFats.text.toString().toInt(),
                        true
                    )

                    if (myPreference.getFirstTimeOpeningApp() == null) {
                        val dailyMacroTargetsService =
                            ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
                        val requestCall = dailyMacroTargetsService.addActiveDailyMacroTargets(
                            newDailyMacroTargets
                        )

                        requestCall.enqueue(object : Callback<DailyMacroTargetsData> {
                            override fun onFailure(
                                call: Call<DailyMacroTargetsData>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    this@SetMyOwnMacrosActivity,
                                    "Failed TO add Item",
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
                                        this@SetMyOwnMacrosActivity,
                                        "Successfully Added",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    myPreference.setFirstTimeOpeningApp("asd")
                                    val activityIntent = Intent(this@SetMyOwnMacrosActivity, HomeActivity::class.java)
                                    startActivity(activityIntent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@SetMyOwnMacrosActivity,
                                        "Failed to add item!!!!!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        })
                    } else {
                        if(Uid != null ) {
                            val dailyMacroTargetsService =
                                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)
                            val requestCall = dailyMacroTargetsService.updateActiveDailyMacroTargets(
                                Uid!!,
                                newDailyMacroTargets
                            )
                            requestCall.enqueue(object : Callback<DailyMacroTargetsData> {
                                override fun onResponse(
                                    call: Call<DailyMacroTargetsData>,
                                    response: Response<DailyMacroTargetsData>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@SetMyOwnMacrosActivity,
                                            "Successfully Updated",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val activityIntent = Intent(this@SetMyOwnMacrosActivity, HomeActivity::class.java)
                                        startActivity(activityIntent)
                                        finish()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@SetMyOwnMacrosActivity,
                                            "UnSuccessfully Updated",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DailyMacroTargetsData>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        this@SetMyOwnMacrosActivity,
                                        "Failed to  Update",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            })
                        } else {
                            Toast.makeText(this@SetMyOwnMacrosActivity, " Connect to internet please", Toast.LENGTH_SHORT).show()
                        }

                    }

                }


            }

        }
        return super.onOptionsItemSelected(item)
    }

}