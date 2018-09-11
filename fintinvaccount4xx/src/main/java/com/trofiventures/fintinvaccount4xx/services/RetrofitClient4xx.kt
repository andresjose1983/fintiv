package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintivaccounts.util.Secrets4xx
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient4xx {

    private var retrofit: Retrofit? = null

    init {
        init()
    }

    private fun init() {
        val builder = OkHttpClient.Builder()

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // baseUrl method
        retrofit = Retrofit.Builder()
                .baseUrl(Secrets4xx.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
    }

    fun get() = retrofit?.create(FintivAccounts4xxApi::class.java)
}
