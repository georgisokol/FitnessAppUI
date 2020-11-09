package com.example.fitnessapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.fitnessapp.R
import com.example.fitnessapp.activities.HomeActivity
import com.example.fitnessapp.utils.MyPreference
import kotlinx.android.synthetic.main.fragment_rest_or_training_dialog.*


class RestOrTrainingDialogFragment : DialogFragment() {
    private lateinit var myPreference: MyPreference
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

        text_training.setOnClickListener {

            (activity as HomeActivity).setTrainingDayTitle()
            (activity as HomeActivity).showMacroCircles()
            myPreference.setTrainingOrRestDay("training")

            dialog?.dismiss()
        }
        text_rest.setOnClickListener {
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