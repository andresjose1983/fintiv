package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintivaccounts3xx.model.request.FintivLogin
import com.trofiventures.fintivaccounts3xx.model.response.Registration
import com.trofiventures.fintivaccounts3xx.model.request.FintivPersonRequest
import com.trofiventures.fintivaccounts3xx.model.response.Logon
import com.trofiventures.fintivaccounts3xx.model.response.SignonInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FintivAccounts3xxApi {

    @POST("person/registration")
    fun personRegistration(@Body personRequest: FintivPersonRequest,
                           @Header("TenantName") tenantName: String): Call<Registration>

    @POST("logon")
    fun logon(@Body login: FintivLogin): Call<Logon>
}