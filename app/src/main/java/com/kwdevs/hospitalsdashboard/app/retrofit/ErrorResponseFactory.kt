package com.kwdevs.hospitalsdashboard.app.retrofit

import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class ErrorResponseFactory(private val moshi: Moshi) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
        return ErrorResponseBodyConverter(adapter)
    }

    private class ErrorResponseBodyConverter<T>(
        private val adapter: JsonAdapter<T>
    ) : Converter<ResponseBody, T> {
        override fun convert(value: ResponseBody): T? {
            return try {
                adapter.fromJson(value.string())
            } catch (e: Exception) {
                null
            }
        }
    }

    companion object {
        fun create(moshi: Moshi): ErrorResponseFactory {
            return ErrorResponseFactory(moshi)
        }
    }
}