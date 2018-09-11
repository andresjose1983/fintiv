package com.trofiventures.fintinvaccount4xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.model.request.*
import com.trofiventures.fintinvaccount4xx.model.response.AddCredentialResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreatePersonResponse
import com.trofiventures.fintinvaccount4xx.model.response.SigonReponse
import com.trofiventures.fintivaccounts.services.RetrofitClient4xx
import com.trofiventures.fintivaccounts.util.Secrets4xx
import org.jetbrains.anko.doAsync

class FintivAccounts4xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient4xx()
    var error: MutableLiveData<String> = MutableLiveData()
    var personCreated: MutableLiveData<CreatePersonResponse> = MutableLiveData()
    var addCredentialResponse: MutableLiveData<AddCredentialResponse> = MutableLiveData()
    var sigonResponse: MutableLiveData<SigonReponse> = MutableLiveData()
    var createAccountResponse: MutableLiveData<CreateAccountResponse> = MutableLiveData()

    fun registerUserWith(name: String,
                         lastName: String,
                         email: String,
                         password: String) {

        if (Secrets4xx.TENANT.isEmpty()) {
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
            val contextRequest = ContextRequest(tenantName = Secrets4xx.TENANT)
            val call = retrofit.get()?.createPerson(CreatePerson(contextRequest,
                    firstName = name,
                    lastName = lastName,
                    password = password))?.execute()

            val body = call?.body()
            if (body != null) {
                if (body.contextResponse.statusCode.equals("SUCCESS")) {
                    personCreated.postValue(body)
                    val callAddPersonCredential = retrofit.get()?.addPersonCredential(PersonCredential(contextRequest,
                            credential = email, personid = body.person.id))?.execute()
                    val bodyAddCredentialResponse = callAddPersonCredential?.body()
                    if (bodyAddCredentialResponse != null) {
                        addCredentialResponse.postValue(bodyAddCredentialResponse)
                    }
                    Log.d("hola", callAddPersonCredential.toString())
                } else {
                    error.postValue(body.contextResponse.statusCode)
                }
            }
        }
    }

    fun sigon(email: String,
              password: String) {

        if (email.isEmpty()) {
            error.value = "error-email"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        doAsync {
            val call = retrofit.get()?.signon(Signon(ContextRequest(Secrets4xx.TENANT),
                    Login("EMAIL", email),
                    Pass("password", password)))?.execute()
            val body = call?.body()
            body?.let {
                FintivAccounts4xx.saveToken(context, it)
                Log.d("hola", it.contextResponse.token + " " + it.contextResponse.tenantName)
                sigonResponse.postValue(it)
            }
            Log.d("hola", call?.message())
        }
    }
}