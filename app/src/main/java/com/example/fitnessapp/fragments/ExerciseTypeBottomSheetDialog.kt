package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.exercise_type_bottom_sheet_layout.*
import java.lang.ClassCastException

class ExerciseTypeBottomSheetDialog: BottomSheetDialogFragment() {
    lateinit var exerciseTypeBottomSheetListener: ExerciseTypeBottomSheetListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exercise_type_bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_walk.setOnClickListener {
            exerciseTypeBottomSheetListener.typeOptionClicked(text_walk.text.toString())
            dialog?.dismiss()
        }
        text_cardio.setOnClickListener {
            exerciseTypeBottomSheetListener.typeOptionClicked(text_cardio.text.toString())
            dialog?.dismiss()
        }
        text_weights.setOnClickListener {
            exerciseTypeBottomSheetListener.typeOptionClicked(text_weights.text.toString())
            dialog?.dismiss()
        }
        text_close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    interface ExerciseTypeBottomSheetListener{
        fun typeOptionClicked(type: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            exerciseTypeBottomSheetListener = context as ExerciseTypeBottomSheetListener
        } catch(e : ClassCastException) {
            throw ClassCastException(context.toString() + "must implement bottom sheet listener")
        }
    }
}