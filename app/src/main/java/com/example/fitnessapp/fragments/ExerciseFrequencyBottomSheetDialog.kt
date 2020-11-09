package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.exercise_frequency_bottom_sheet_layout.*
import java.lang.ClassCastException

class ExerciseFrequencyBottomSheetDialog: BottomSheetDialogFragment() {
    lateinit var exerciseFrequencyBottomSheetListener: ExerciseFrequencyBottomSheetListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.exercise_frequency_bottom_sheet_layout,container,false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_rarely.setOnClickListener {
            exerciseFrequencyBottomSheetListener.onFrequencyOptionClicked(text_rarely.text.toString())
            dialog?.dismiss()
        }
        text_1_3.setOnClickListener {
            exerciseFrequencyBottomSheetListener.onFrequencyOptionClicked(text_1_3.text.toString())
            dialog?.dismiss()
        }
        text_3_5.setOnClickListener {
            exerciseFrequencyBottomSheetListener.onFrequencyOptionClicked(text_3_5.text.toString())
            dialog?.dismiss()
        }
        text_6_7.setOnClickListener {
            exerciseFrequencyBottomSheetListener.onFrequencyOptionClicked(text_6_7.text.toString())
            dialog?.dismiss()
        }
        text_close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    interface ExerciseFrequencyBottomSheetListener{
        fun onFrequencyOptionClicked(frequency: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            exerciseFrequencyBottomSheetListener = context as ExerciseFrequencyBottomSheetListener
        } catch(e :ClassCastException) {
                throw ClassCastException(context.toString() + "must implement bottom sheet listener")
        }

    }
}