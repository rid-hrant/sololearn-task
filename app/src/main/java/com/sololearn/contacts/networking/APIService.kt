package com.sololearn.contacts.networking

import com.sololearn.contacts.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIService {

    private const val QUERY_NAME_API_KEY = "apiKey"
    private val service: SoloLearnService

    init {
        val clientBuilder = OkHttpClient.Builder().addInterceptor {
            var request = it.request()
            val url = request.url().newBuilder().addQueryParameter(QUERY_NAME_API_KEY, BuildConfig.API_KEY).build()
            request = request.newBuilder().url(url).build()
            it.proceed(request)
        }

        if (BuildConfig.isRelease.not()) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()

        service = retrofit.create(SoloLearnService::class.java)
    }

    fun getInstance(): SoloLearnService {
        return service
    }
}