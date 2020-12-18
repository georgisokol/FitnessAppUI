package com.example.fitnessapp.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.activities.HomeActivity
import com.example.fitnessapp.services.DailyMacroTargetsService
import com.example.fitnessapp.services.ServiceBuilder
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.MacrosViewModel
import kotlinx.android.synthetic.main.fragment_rest_or_training_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class RestOrTrainingDialogFragment : DialogFragment() {
    private lateinit var myPreference: MyPreference
    private val macrosViewModel: MacrosViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        myPreference= MyPreference(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rest_or_training_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)

        val dailyMacroTargetsService = ServiceBuilder.buildService(DailyMacroTargetsService::class.java)

        text_training.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {


                    val response = dailyMacroTargetsService.updateTrainingDay(myPreference.getUserUid()!! , myPreference.getDailyMacroTargetsUid()!! , true)
                    if(response.isSuccessful) {
                        val dailyTargets = response.body()
                        Log.i("Updejtiran", dailyTargets?.isTrainingDay.toString())
                    } else {
                        Log.i("FAIL", "NE USPEAAAAA")
                    }

            }
            (activity as HomeActivity).setTrainingDayTitle()
            (activity as HomeActivity).showMacroCircles()
            myPreference.setTrainingOrRestDay("training")
            dialog?.dismiss()
        }
        text_rest.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val response = dailyMacroTargetsService.updateTrainingDay(myPreference.getUserUid()!! , myPreference.getDailyMacroTargetsUid()!! , false)
                if(response.isSuccessful) {
                    val dailyTargets = response.body()
                    Log.i("Updejtiran", dailyTargets?.isTrainingDay.toString())
                } else {
                    Log.i("FAIL", "NE USPEAAAAA")
                }
            }

            (activity as HomeActivity).setRestDayTitle()
            (activity as HomeActivity).showMacroCircles()
            myPreference.setTrainingOrRestDay("rest")
            dialog?.dismiss()
        }

    }
    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.rest_or_training_day_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.rest_or_training_day_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rest_or_training_day_dialog_backround)
    }
}