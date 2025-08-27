package com.kwdevs.hospitalsdashboard.app.retrofit

import android.util.Log
import com.kwdevs.hospitalsdashboard.controller.serverError
import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException

class ErrorParser {
    fun parseError(errorBody:String): ErrorResponse?{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        Log.e("Controller Error",errorBody)
        return adapter.fromJson(errorBody)
    }
    fun errorResponse(e:Exception): ErrorResponse {
        val error = when (e) {
            is HttpException -> {
                val errorBody = e.response()?.errorBody()?.string()?:""
                val parsedError = try {ErrorParser().parseError(errorBody)}
                catch (parseEx: Exception) { null }
                parsedError ?: serverError()
            }
            else -> ErrorResponse(code = 505, message = e.message ?: "Unknown error", errors = null)
        }
        return error
    }
}