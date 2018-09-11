package com.trofiventures.fintivaccounts3xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.trofiventures.fintivaccounts.model.FintivPerson
import com.trofiventures.fintivaccounts.services.RetrofitClient3xx
import com.trofiventures.fintivaccounts.util.Secrets3xx
import com.trofiventures.fintivaccounts3xx.FintivAccounts3xx
import com.trofiventures.fintivaccounts3xx.model.request.FintivLogin
import com.trofiventures.fintivaccounts3xx.model.request.FintivPersonCredentials
import com.trofiventures.fintivaccounts3xx.model.request.FintivPersonRequest
import com.trofiventures.fintivaccounts3xx.model.response.Logon
import com.trofiventures.fintivaccounts3xx.model.response.Registration
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FintivAccounts3xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient3xx()
    var error: MutableLiveData<String> = MutableLiveData()
    var registration: MutableLiveData<Registration> = MutableLiveData()
    var logon: MutableLiveData<Logon> = MutableLiveData()

    fun registerUserWith(name: String,
                         lastName: String,
                         email: String,
                         password: String) {

        if (Secrets3xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        if (name.isEmpty()) {
            error.value = "error_name"
            return
        }

        if (lastName.isEmpty()) {
            error.value = "error_lastName"
            return
        }

        if (email.isEmpty()) {
            error.value = "error_email"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        doAsync {
            val fintivPerson = FintivPerson(firstName = name,
                    lastName = lastName, password = password)
            val credentials = FintivPersonCredentials(credential = email)
            val credentialss = ArrayList<FintivPersonCredentials>()
            credentialss.add(credentials)
            val request = FintivPersonRequest(person = fintivPerson, personCredentials = credentialss)

            retrofit.get()?.personRegistration(request, Secrets3xx.TENANT)?.enqueue(object : Callback<Registration> {
                override fun onFailure(call: Call<Registration>, t: Throwable) {
                    Log.d("hola", t.message)
                }

                override fun onResponse(call: Call<Registration>, response: Response<Registration>) {
                    if (response.body() != null) {
                        registration.postValue(response.body())
                        Log.d("hola si", response.body()?.personRegistrationResponse?.contextResponse?.statusMessage)
                    } else {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), Registration::class.java)

                        error.postValue(errorResponse.personRegistrationResponse.contextResponse.statusMessage)
                    }
                }
            })
        }
    }

    fun login(username: String, password: String) {
        if (Secrets3xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        if (username.isEmpty()) {
            error.value = "error_user"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        doAsync {
            val call = retrofit.get()?.logon(FintivLogin(tenantName = Secrets3xx.TENANT,
                    login = username, pass = password))?.execute()
            val body = call?.body()
            if (body != null) {
                if(body.person != null && body.signonInfo != null){
                    body.let {
                        FintivAccounts3xx.saveToken(context, it)
                        logon.postValue(it)
                    }
                }else{
                    error.postValue("invalid_credentials")
                }
            }
        }
    }
}