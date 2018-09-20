package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintinvaccount4xx.model.request.CreditCardCardConnect
import com.trofiventures.fintinvaccount4xx.model.response.CreditCardCardConnectToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface CardConnectApi {

    @PUT("rest/auth")
    fun generateCreditCardToken(@Header("Authorization") authorization: String,
                                @Body creditCardCardConnect: CreditCardCardConnect): Call<CreditCardCardConnectToken>
}