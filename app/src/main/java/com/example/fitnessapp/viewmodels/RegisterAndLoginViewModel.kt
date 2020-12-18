package com.example.fitnessapp.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.dataclasses.RegisterAndLoginResponseObject
import com.example.fitnessapp.dataclasses.UserAndPassObject
import com.example.fitnessapp.fragments.ServerOrConnectivityErrorDialog
import com.example.fitnessapp.interfaces.LoginInterface
import com.example.fitnessapp.interfaces.RegisterInterface
import com.example.fitnessapp.interfaces.ServerAndConnectivityInterface
import com.example.fitnessapp.network.NoConnectivityException
import com.example.fitnessapp.repositories.RegisterAndLoginRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_login_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

class RegisterAndLoginViewModel(application: Application) : AndroidViewModel(application) {

    private var registerAndLoginRepository: RegisterAndLoginRepository =
        RegisterAndLoginRepository(application)

    var registerInterface: RegisterInterface? = null
    var loginInterface :LoginInterface? = null
    var serverOrConnectivityInterface : ServerAndConnectivityInterface? = null

    fun registerUser(user: UserAndPassObject) {

            viewModelScope.launch(Dispatchers.Main) {
                try {
                    val response = registerAndLoginRepository.registerUser(user)
                    if (response.code() == 200) {
                        registerInterface?.onSuccess(response.body()?.message!!)
                    } else if (response.code() == 404) {
                        registerInterface?.onFailureUsernameTaken(response.errorBody()?.string()!!)
                    }
                } catch (e: NoConnectivityException) {
                    Log.e("Connectivity", e.message)
                    serverOrConnectivityInterface?.onConnectivityError()
                } catch( e: SocketTimeoutException ) {
                    Log.e("Server status", "SERVER IS DOWN")
                    serverOrConnectivityInterface?.onServerError()
                }


            }


    }

    fun  loginUser(user: UserAndPassObject) {

            viewModelScope.launch(Dispatchers.Main) {
                try {
                    val response = registerAndLoginRepository.loginUser(user)
                    if(response.code() == 200) {
                        loginInterface?.onLoginSuccess(response.body()?.message!!)
                    } else if(response.code() == 404) {
                        val gson = Gson()
                        val type = object : TypeToken<RegisterAndLoginResponseObject>() {}.type
                        val errorResponseString: RegisterAndLoginResponseObject = gson.fromJson(response.errorBody()?.charStream(), type)
                        if(errorResponseString.message.trim().contains("username"))
                        {
                            loginInterface?.onFailureUsernameDoesNotExist(errorResponseString.message)
                        } else if(errorResponseString.message.trim().contains("Password")) {
                            loginInterface?.onFailureWrongPassword(errorResponseString.message)
                        }
                    }
                } catch (e: NoConnectivityException) {
                    Log.e("Connectivity", e.message)
                    serverOrConnectivityInterface?.onConnectivityError()
                } catch( e: SocketTimeoutException ) {
                    Log.e("Server status", "SERVER IS DOWN")
                    serverOrConnectivityInterface?.onServerError()
                }


            }



    }


}