package com.kwdevs.hospitalsdashboard.app.retrofit

import com.facebook.stetho.okhttp3.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kwdevs.hospitalsdashboard.routes.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private val client              : OkHttpClient = buildClient()
    private val retrofit            = buildRetrofit()
    private val connectException    = MutableStateFlow(false)
    //private val loggingInterceptor  = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY}

    private fun resetConnectException() { connectException.value = false }
    private fun setConnectException() { connectException.value = true }

    private fun buildClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                resetConnectException()
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

                    .addHeader("Connection", "close")
                    .build()

                try {
                    val connection  = chain.connection()
                    if (connection != null && connection.socket().isConnected) {
                        chain.proceed(request)
                    } else {
                        setConnectException()
                        chain.proceed(request)
                    }
                } catch (e: Exception) {
                    setConnectException()
                    e.printStackTrace()
                    chain.proceed(request)
                }
            }

        if (BuildConfig.DEBUG) { builder.addNetworkInterceptor(StethoInterceptor()) }
        //try{ builder.addInterceptor(loggingInterceptor) }catch (e:Exception){ e.printStackTrace() }
        return builder.build()
    }
    private fun buildRetrofit(): Retrofit {
        val factory     = KotlinJsonAdapterFactory()
        val moshi       = Moshi.Builder().add(UriJsonAdapter()).add(factory).build()
        val cf          = MoshiConverterFactory.create(moshi)
        val erf         = ErrorResponseFactory.create(moshi)
        val callAdapter = CoroutineCallAdapterFactory()

        return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(cf)
                .addCallAdapterFactory(callAdapter) // For coroutine support
                .addConverterFactory(erf).build()
    }

    fun <T> createService(service: Class<T>): T { return retrofit.create(service) }
}
