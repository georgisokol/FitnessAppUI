package com.example.fitnessapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.fitnessapp.R
import com.example.fitnessapp.activities.CalculateMacrosActivity
import com.example.fitnessapp.activities.SetMyOwnMacrosActivity
import kotlinx.android.synthetic.main.fragment_calculate_or_enter_macros_dialog.*



class CalculateOrEnterMacrosDialog: DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_calculate_or_enter_macros_dialog,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.ShakingDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)


        text_calculate.setOnClickListener {
            val activityIntent = Intent(requireContext(), CalculateMacrosActivity::class.java)
            startActivity(activityIntent)
            dialog?.dismiss()
        }

        text_enter.setOnClickListener {
            val activityIntent = Intent(requireContext(), SetMyOwnMacrosActivity::class.java)
            startActivity(activityIntent)
            dialog?.dismiss()
        }
        text_close.setOnClickListener{

            dialog?.dismiss()
        }
    }
    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.calculate_or_enter_macros_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.calculate_or_enter_macros_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rest_or_training_day_dialog_backround)
    }
}