package com.trofiventures.fintinvaccount4xx

import android.content.Context
import com.cardconnect.consumersdk.CCConsumer
import com.google.gson.Gson
import com.trofiventures.fintinvaccount4xx.model.response.SingOnResponse
import com.trofiventures.fintivaccounts.util.Secrets4xx


object FintivAccounts4xx {

    private val TOKEN = "FintivAccounts4xx-Fintiv4xxToken"
    private val FINTIV = "Fintiv"

    fun setupWithTenant(tenant: String,
                        endPoint: String? = null) {
        endPoint?.let {
            Secrets4xx.ENDPOINT = it
        }

        tenant.let {
            Secrets4xx.TENANT = it
        }
    }

    fun setupWithCardConnect(endPoint: String, merchantId: String, user: String, password: String) {

        endPoint.let {
            Secrets4xx.CARD_CONNECT = it
            //CCConsumer.getInstance().api.setEndPoint(it)
            //CCConsumer.getInstance().api.setDebugEnabled(true)
        }

        merchantId.let {
            Secrets4xx.MERCHANT_ID = it
        }

        user.let {
            Secrets4xx.USER = it
        }

        password.let {
            Secrets4xx.PASSWORD = it
        }

    }

   // fun cardConnect() = CCConsumer.getInstance().api

    fun saveToken(context: Context, singOnResponse: SingOnResponse) {
        context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).edit()?.apply {
            putString(TOKEN, Gson().toJson(singOnResponse))
        }?.apply()
    }

    fun currentToken(context: Context): SingOnResponse? {
        return Gson().fromJson(context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).getString(TOKEN, null), SingOnResponse::class.java)
    }

}