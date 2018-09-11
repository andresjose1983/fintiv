package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintivaccounts.model.AccountItem
import com.trofiventures.fintivaccounts.model.FintivPerson
import com.trofiventures.fintivaccounts.model.FintivToken
import retrofit2.Call
import retrofit2.http.*

interface FintivAccountsApi {

    @POST("signup")
    fun signUp(@Query("firstName") firstName: String,
               @Query("lastName") lastName: String,
               @Query("password") password: String,
               @Query("username") username: String,
               @Query("walletId") walletId: String): Call<FintivPerson>

    @FormUrlEncoded
    @POST("oauth/token")
    fun login(@Header("Content-Type") ct: String = "application/x-www-form-urlencoded",
              @Header("Accept") accept: String = "application/json",
              @Header("Authorization") auth: String,
              @Field("grant_type") gt: String,
              @Field("username") username: String,
              @Field("password") password: String): Call<FintivToken>

    @GET("oauth/myinfo")
    fun getAccountInfo(@Header("Authorization") auth: String): Call<Any>

    @PUT("oauth/account/{user}")
    fun updateAccountInfo(@Header("Authorization") auth: String,
                          @Body accountItem: AccountItem,
                          @Path("user") user: String): Call<Any>
}