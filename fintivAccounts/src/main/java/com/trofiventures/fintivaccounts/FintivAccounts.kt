package com.trofiventures.fintivaccounts

import android.content.Context
import com.auth0.android.jwt.JWT
import com.squareup.moshi.Moshi
import com.trofiventures.fintivaccounts.model.FintivToken
import com.trofiventures.fintivaccounts.util.Secrets
import java.util.*


object FintivAccounts {

    private val TOKEN = "FintivAccounts-FintivToken"
    private val FINTIV = "Fintiv"

    fun setupWithWalletId(walletId: String,
                          endPoint: String?,
                          clientId: String,
                          clientSecret: String) {
        endPoint?.let {
            Secrets.WALLETID = it
        }

        walletId.let {
            Secrets.WALLETID = it
        }

        clientId.let {
            Secrets.CLIENTID = it
        }

        clientSecret.let {
            Secrets.CLIENTSECRET = it
        }
    }

    fun saveToken(context: Context,
                  fintivToken: FintivToken?) {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<FintivToken>(FintivToken::class.java)
        context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).edit().apply {
            putString(TOKEN, jsonAdapter.toJson(fintivToken))
        }.apply()
    }

    fun getToken(context: Context): FintivToken? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<FintivToken>(FintivToken::class.java)
        val json = context.getSharedPreferences(FINTIV, Context.MODE_PRIVATE).getString(TOKEN, null)
        if (json != null)
            return jsonAdapter.fromJson(json)
        return null
    }

    fun jwtAsString(context: Context, claimValue: String): String? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asString()
        }
        return null
    }

    fun jwtAsInt(context: Context, claimValue: String): Int? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asInt()
        }
        return null
    }

    fun jwtAsBoolean(context: Context, claimValue: String): Boolean? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asBoolean()
        }
        return null
    }

    fun jwtAsDouble(context: Context, claimValue: String): Double? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asDouble()
        }
        return null
    }

    fun jwtAsDate(context: Context, claimValue: String): Date? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asDate()
        }
        return null
    }

    fun <T> jwtAsArray(context: Context, claimValue: String, clazz: Class<T>): List<T>? {
        val token = getToken(context)
        if (token != null) {
            val jwt = JWT(token.access_token)
            return jwt.getClaim(claimValue).asList(clazz)
        }
        return null
    }
}