package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.fitnessapp.R
import com.example.fitnessapp.utils.ARGUMENTS_SERVER_CONNECTIVITY_ERROR_TEXT
import com.example.fitnessapp.utils.MyPreference
import kotlinx.android.synthetic.main.fragment_server_or_connectivity_error_dialog.*

class ServerOrConnectivityErrorDialog: DialogFragment() {
    private  var errorMessage:String? = null
    private lateinit var myPreference: MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        myPreference= MyPreference(requireContext())
        errorMessage = arguments?.getString(ARGUMENTS_SERVER_CONNECTIVITY_ERROR_TEXT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_server_or_connectivity_error_dialog, container, false)
    }
    fun newInstance(errorMessage: String): ServerOrConnectivityErrorDialog {
        val args = Bundle()
        args.putString(ARGUMENTS_SERVER_CONNECTIVITY_ERROR_TEXT, errorMessage)
        val fragment = ServerOrConnectivityErrorDialog()
        fragment.arguments = args
        return fragment

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_errorMessage.text = errorMessage


        text_close.setOnClickListener {
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