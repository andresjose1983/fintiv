package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintinvaccount4xx.model.request.*
import com.trofiventures.fintinvaccount4xx.model.response.AddCredentialResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreatePersonResponse
import com.trofiventures.fintinvaccount4xx.model.response.SigonReponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FintivAccounts4xxApi {

    @POST("createPerson")
    fun createPerson(@Body createPerson: CreatePerson): Call<CreatePersonResponse>

    @POST("addPersonCredential")
    fun addPersonCredential(@Body personCredential: PersonCredential): Call<AddCredentialResponse>

    @POST("signon")
    fun signon(@Body signon: Signon): Call<SigonReponse>

    @POST("createAccount")
    fun createAccount(@Body createAccount: CreateAccount): Call<CreateAccountResponse>

    @POST("addMoneyContainer")
    fun addMoneyContainer(@Body moneyContainer: MoneyContainer): Call<CreateAccountResponse>
}