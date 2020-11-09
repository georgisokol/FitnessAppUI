package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.gender_bottom_sheet_layout.*
import java.lang.ClassCastException

class GenderBottomSheetDialog: BottomSheetDialogFragment() {
    lateinit var genderBottomSheetListener: GenderBottomSheetListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gender_bottom_sheet_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_male.setOnClickListener {
            genderBottomSheetListener.genderOptionClicked(text_male.text.toString())
            dialog?.dismiss()
        }
        text_female.setOnClickListener {
            genderBottomSheetListener.genderOptionClicked(text_female.text.toString())
            dialog?.dismiss()
        }
        text_close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            genderBottomSheetListener = context as GenderBottomSheetListener
        } catch(e : ClassCastException) {
            throw ClassCastException(context.toString() + "must implement bottom sheet listener")
        }
    }

    interface GenderBottomSheetListener {
        fun genderOptionClicked(gender: String)
    }
}