package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.exercise_goal_bottom_sheet_layout.*
import java.lang.ClassCastException

class ExerciseGoalBottomSheetDialog: BottomSheetDialogFragment() {
lateinit var exerciseGoalBottomSheetListener: ExerciseGoalBottomSheetListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exercise_goal_bottom_sheet_layout, container, false)
    }

    interface ExerciseGoalBottomSheetListener {
        fun goalOptionClicked(goal: String)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_loseFat.setOnClickListener {
            exerciseGoalBottomSheetListener.goalOptionClicked(text_loseFat.text.toString())
            dialog?.dismiss()
        }
        text_buildMuscle.setOnClickListener {
            exerciseGoalBottomSheetListener.goalOptionClicked(text_buildMuscle.text.toString())
            dialog?.dismiss()
        }
        text_staySame.setOnClickListener {
            exerciseGoalBottomSheetListener.goalOptionClicked(text_staySame.text.toString())
            dialog?.dismiss()
        }
        text_close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            exerciseGoalBottomSheetListener = context as ExerciseGoalBottomSheetListener
        } catch (e :ClassCastException) {
            throw ClassCastException(context.toString() + "bottom sheet ")
        }

    }



}