package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.activities.HomeActivity
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.MacrosViewModel
import kotlinx.android.synthetic.main.fragment_rest_day_suggestion_dialog.*


class RestDaySuggestionDialogFragment :DialogFragment(){
    private lateinit var myPreference: MyPreference
    private val macrosViewModel: MacrosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreference = MyPreference(requireContext())
        dialog?.setCanceledOnTouchOutside(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rest_day_suggestion_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        text_setRestDay.setOnClickListener {
            macrosViewModel.updateTrainingDay(myPreference.getUserUid()!!, myPreference.getDailyMacroTargetsUid()!!, false)

            (activity as HomeActivity).setRestDayTitle()
            (activity as HomeActivity).showMacroCircles()
            myPreference.setTrainingOrRestDay("rest")
            myPreference.setRestDaySuggestion(false)
            dialog?.dismiss()
        }


        text_setTrainingDay.setOnClickListener {
            macrosViewModel.updateTrainingDay(myPreference.getUserUid()!!, myPreference.getDailyMacroTargetsUid()!!, true)

            (activity as HomeActivity).setTrainingDayTitle()
            (activity as HomeActivity).showMacroCircles()
            myPreference.setTrainingOrRestDay("training")
            myPreference.setRestDaySuggestion(false)
            dialog?.dismiss()
        }

    }
    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.rest_or_training_day_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.rest_suggestion_day_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rest_or_training_day_dialog_backround)
    }


}