package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintinvaccount4xx.model.request.*
import com.trofiventures.fintinvaccount4xx.model.request.MoneyContainer
import com.trofiventures.fintinvaccount4xx.model.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FintivAccounts4xxApi {

    @POST("createPerson")
    fun createPerson(@Body createPerson: CreatePerson): Call<CreatePersonResponse>

    @POST("addPersonCredential")
    fun addPersonCredential(@Body personCredential: PersonCredential): Call<AddCredentialResponse>

    @POST("signon")
    fun signon(@Body signon: Signon): Call<SingOnResponse>

    @POST("createAccount")
    fun createAccount(@Body createAccount: CreateAccount): Call<CreateAccountResponse>

    @POST("addMoneyContainer")
    fun addMoneyContainer(@Body moneyContainer: MoneyContainer): Call<CreateAccountResponse>

    @POST("getMoneyContainers")
    fun getMoneyContainers(@Body sendToken: SendToken): Call<MoneyContainerResponse>

    @POST("getPerson")
    fun getPerson(@Body sendToken: SendToken): Call<PersonResponse>

    @POST("removeMoneyContainer")
    fun removeMoneyContainer(@Body removeMoneyContainer: RemoveMoneyContainer): Call<RemoveMoneyContainerResponse>
}