package com.example.studybuddy.di

import com.example.studybuddy.data.locale.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val dataStoreManager: DataStoreManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            dataStoreManager.tokenFlow.first()
        }

        if (token.isNullOrEmpty() || chain.request().toString().contains("/auth/"))
            return chain.proceed(chain.request())

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}