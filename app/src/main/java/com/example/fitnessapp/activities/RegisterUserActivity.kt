package com.example.fitnessapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.RegisterAndLoginResponseObject
import com.example.fitnessapp.dataclasses.UserAndPassObject
import com.example.fitnessapp.fragments.ServerOrConnectivityErrorDialog
import com.example.fitnessapp.interfaces.RegisterInterface
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.viewmodels.RegisterAndLoginViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_register_user.*


class RegisterUserActivity : AppCompatActivity(), RegisterInterface, ServerAndConnectivityInterface {
    private val registerViewModel: RegisterAndLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        registerViewModel.registerInterface = this
        registerViewModel.serverOrConnectivityInterface = this

        button_register.setOnClickListener {
            val username = input_username.text.toString()
            val password = input_password.text.toString()
            val userAndPassObject = UserAndPassObject(username, password)

            if (username.length < 5) {
                username_wrapper.error = "The username must contain at least 5 characters"
            } else if (password.length < 5) {
                password_wrapper.error = "The password must contain at least 5 characters"
            } else {
                registerViewModel.registerUser(userAndPassObject)
            }
        }

    }

    override fun onSuccess(message: String) {
        Toast.makeText(this@RegisterUserActivity, message, Toast.LENGTH_SHORT).show()
        val activityIntent = Intent(this@RegisterUserActivity, LoginUserActivity::class.java)
        startActivity(activityIntent)
        finish()
    }

    override fun onFailureUsernameTaken(message: String) {
        val gson = Gson()
        val type = object : TypeToken<RegisterAndLoginResponseObject>() {}.type
        var errorResponseString: RegisterAndLoginResponseObject = gson.fromJson(message, type)
        username_wrapper.error = errorResponseString.message
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