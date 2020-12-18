package com.example.fitnessapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.DailyMacroTargetsData
import com.example.fitnessapp.fragments.ServerOrConnectivityErrorDialog
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.network.NoConnectivityException
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.MacrosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_set_my_own_macros.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException


class SetMyOwnMacrosActivity : AppCompatActivity(), ServerAndConnectivityInterface {
    private lateinit var myPreference: MyPreference
    private var Uid: String? = null
    private val macrosViewModel: MacrosViewModel by viewModels()
    lateinit var serverOrConnectivityInterface :ServerAndConnectivityInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_own_macros)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_cancel_24)

        myPreference = MyPreference(this)

        serverOrConnectivityInterface = this
        macrosViewModel.serverOrConnectivityInterface = this


        if (myPreference.getFirstTimeOpeningApp() != null) {
            macrosViewModel.getDailyMacroTargets(myPreference.getUserUid()!!)

            macrosViewModel.dailyMacroTargetsLive?.observe(this, Observer {
                val dailyMacroTargetsData = it
                if (dailyMacroTargetsData != null) {
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
                        try {
                            val requestCall = dailyMacroTargetsService.addActiveDailyMacroTargets(
                                myPreference.getUserUid()!!,
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

                                        val activityIntent = Intent(
                                            this@SetMyOwnMacrosActivity,
                                            HomeActivity::class.java
                                        )
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
                        } catch (e: NoConnectivityException) {
                            Log.e("Connectivity", e.message)
                            serverOrConnectivityInterface.onConnectivityError()
                        } catch( e: SocketTimeoutException) {
                            Log.e("Server status", "SERVER IS DOWN")
                            serverOrConnectivityInterface.onServerError()
                        }

                    } else {
                        if (Uid != null) {
                            val dailyMacroTargetsService =
                                ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

                            try {
                                val requestCall =
                                    dailyMacroTargetsService.updateActiveDailyMacroTargets(
                                        myPreference.getUserUid()!!,
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
                                            val activityIntent = Intent(
                                                this@SetMyOwnMacrosActivity,
                                                HomeActivity::class.java
                                            )
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
                            } catch (e: NoConnectivityException) {
                                Log.e("Connectivity", e.message)
                                serverOrConnectivityInterface.onConnectivityError()
                            } catch( e: SocketTimeoutException ) {
                                Log.e("Server status", "SERVER IS DOWN")
                                serverOrConnectivityInterface.onServerError()
                            }

                        } else {
                            Toast.makeText(
                                this@SetMyOwnMacrosActivity,
                                " Connect to internet please",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }


            }

        }
        return super.onOptionsItemSelected(item)
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