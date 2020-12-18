package com.example.fitnessapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.dataclasses.UserAndPassObject
import com.example.fitnessapp.fragments.ServerOrConnectivityErrorDialog
import com.example.fitnessapp.interfaces.LoginInterface
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.RegisterAndLoginViewModel
import kotlinx.android.synthetic.main.activity_login_user.*
import kotlinx.android.synthetic.main.activity_login_user.button_register
import kotlinx.android.synthetic.main.activity_login_user.input_password
import kotlinx.android.synthetic.main.activity_login_user.input_username
import kotlinx.android.synthetic.main.activity_login_user.password_wrapper
import kotlinx.android.synthetic.main.activity_login_user.username_wrapper




class LoginUserActivity : AppCompatActivity(),LoginInterface, ServerAndConnectivityInterface {
    lateinit var myPreference: MyPreference
    val loginViewModel: RegisterAndLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        myPreference = MyPreference(this)
        loginViewModel.loginInterface = this
        loginViewModel.serverOrConnectivityInterface = this

        if(myPreference.getUserUid() != null ){
            val activityIntent = Intent(this@LoginUserActivity, HomeActivity::class.java)
            startActivity(activityIntent)
        }

        button_register.setOnClickListener {
            val activityIntent = Intent(this@LoginUserActivity, RegisterUserActivity::class.java)
            startActivity(activityIntent)
            finish()
        }
        button_login.setOnClickListener {
            val username = input_username.text.toString()
            val password =  input_password.text.toString()
            val userAndPassObject = UserAndPassObject(username, password)

            if(username.length < 5) {
                username_wrapper.error = "The username must contain at least 5 characters"
                password_wrapper.isErrorEnabled = false
            } else if(password.length <5) {
                password_wrapper.error = "The password must contain at least 5 characters"
                username_wrapper.isErrorEnabled = false
            } else {
                loginViewModel.loginUser(userAndPassObject)
            }
        }


    }

    override fun onLoginSuccess(message: String) {
        myPreference.setUserUid(message)
        Toast.makeText(this@LoginUserActivity, message, Toast.LENGTH_SHORT).show()
        val activityIntent = Intent(this@LoginUserActivity, HomeActivity::class.java)
        startActivity(activityIntent)
        finish()
    }

    override fun onFailureUsernameDoesNotExist(message: String) {
        username_wrapper.error = message
        password_wrapper.isErrorEnabled = false
    }

    override fun onFailureWrongPassword(message: String) {
        password_wrapper.error = message
        username_wrapper.isErrorEnabled = false
    }

    override fun onServerError() {
        val errorMessage = "Could not connect to server!"
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