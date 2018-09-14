package com.trofiventures.fintinvaccount4xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.model.Attribute
import com.trofiventures.fintinvaccount4xx.model.request.ContextRequest
import com.trofiventures.fintinvaccount4xx.model.request.CreateAccount
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintivaccounts.services.RetrofitClient4xx
import com.trofiventures.fintivaccounts.util.Secrets4xx
import org.jetbrains.anko.doAsync

class FintivSVA4xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient4xx()
    var error: MutableLiveData<String> = MutableLiveData()
    var createAccountResponse: MutableLiveData<CreateAccountResponse> = MutableLiveData()

    fun createAccount(accountName: String,
                      currency: String) {

        if (accountName.isEmpty()) {
            error.value = "error_account_name"
            return
        }

        if (currency.isEmpty()) {
            error.value = "error_currency"
            return
        }

        val token = FintivAccounts4xx.currentToken(context)
        if (token == null) {
            error.value = "error_security"
            return
        }

        doAsync {
            val contextRequest = ContextRequest(Secrets4xx.TENANT, token.contextResponse.token)
            val createAccount = CreateAccount(arrayListOf(Attribute(key = "ACCOUNT_NAME", value = accountName)),
                    contextRequest = contextRequest, currency = currency)
            val call = retrofit.get()?.createAccount(createAccount)?.execute()
            val body = call?.body()
            if (body != null)
                createAccountResponse.postValue(body)
            else
                error.postValue(body?.contextResponse?.statusCode)
        }
    }
}