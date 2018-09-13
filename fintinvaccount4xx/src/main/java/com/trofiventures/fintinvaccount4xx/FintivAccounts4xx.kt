package com.trofiventures.fintinvaccount4xx

import android.content.Context
import com.cardconnect.consumersdk.CCConsumer
import com.google.gson.Gson
import com.trofiventures.fintinvaccount4xx.model.response.SigonReponse
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

    fun setupWithCardConnect(endPoint: String) {

        endPoint.let {
            Secrets4xx.CARD_CONNECT = it

            CCConsumer.getInstance().api.setEndPoint(it)
            CCConsumer.getInstance().api.setDebugEnabled(true)
        }
    }

    fun cardConnect() = CCConsumer.getInstance().api

    fun saveToken(context: Context, sigonReponse: SigonReponse) {
        context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).edit()?.apply {
            putString(TOKEN, Gson().toJson(sigonReponse))
        }?.apply()
    }

    fun currentToken(context: Context): SigonReponse? {
        return Gson().fromJson(context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).getString(TOKEN, null), SigonReponse::class.java)
    }

}