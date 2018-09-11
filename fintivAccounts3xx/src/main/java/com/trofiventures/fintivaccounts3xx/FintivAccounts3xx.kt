package com.trofiventures.fintivaccounts3xx

import android.content.Context
import com.google.gson.Gson
import com.trofiventures.fintivaccounts.util.Secrets3xx
import com.trofiventures.fintivaccounts3xx.model.response.Logon


object FintivAccounts3xx {

    private val TOKEN = "FintivAccounts3xx-Fintiv3xxToken"
    private val FINTIV = "Fintiv3xx"

    fun setupWithTenant(tenant: String,
                        endPoint: String? = null) {
        endPoint?.let {
            Secrets3xx.ENDPOINT = it
        }

        tenant.let {
            Secrets3xx.TENANT = it
        }
    }

    fun saveToken(context: Context, logon: Logon) {
        context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).edit().apply {
            putString(TOKEN, Gson().toJson(logon))
        }.apply()
    }

    fun currentToken(context: Context): Logon? {
        return Gson().fromJson<Logon>(context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).getString(TOKEN, null), Logon::class.java)
    }

}