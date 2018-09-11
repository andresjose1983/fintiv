package com.trofiventures.fintivaccounts.services

import com.trofiventures.fintivaccounts.util.Secrets
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {

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
                .baseUrl(Secrets.ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(builder.build())
                .build()
    }

    fun get() = retrofit?.create(FintivAccountsApi::class.java)
}
