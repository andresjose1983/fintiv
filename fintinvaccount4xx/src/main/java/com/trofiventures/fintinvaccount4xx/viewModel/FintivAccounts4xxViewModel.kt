package com.trofiventures.fintinvaccount4xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.model.request.*
import com.trofiventures.fintinvaccount4xx.model.response.AddCredentialResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreatePersonResponse
import com.trofiventures.fintinvaccount4xx.model.response.SingOnResponse
import com.trofiventures.fintivaccounts.services.RetrofitClient4xx
import com.trofiventures.fintivaccounts.util.Secrets4xx
import org.jetbrains.anko.doAsync

class FintivAccounts4xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient4xx()
    var error: MutableLiveData<String> = MutableLiveData()
    var personCreated: MutableLiveData<CreatePersonResponse> = MutableLiveData()
    var addCredentialResponse: MutableLiveData<AddCredentialResponse> = MutableLiveData()
    var singOnResponse: MutableLiveData<SingOnResponse> = MutableLiveData()
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
                } else
                    error.postValue(body.contextResponse.statusCode)
            }
        }
    }

    fun singOn(email: String,
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
            if (body != null) {
                val contextRequest = ContextRequest(Secrets4xx.TENANT, body.contextResponse.token)
                var callPerson = retrofit.get()?.getPerson(SendToken(contextRequest))?.execute()
                callPerson?.body()?.let {
                    it.person.let {
                        body.person = it
                        FintivAccounts4xx.saveToken(context, body)
                        singOnResponse.postValue(body)
                    }
                }
            } else
                error.postValue(body?.contextResponse?.statusCode)
        }
    }
}