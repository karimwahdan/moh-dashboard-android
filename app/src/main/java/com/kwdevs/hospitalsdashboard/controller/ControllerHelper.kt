package com.kwdevs.hospitalsdashboard.controller

import android.util.Log
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse


fun emptyError(): ErrorResponse {return ErrorResponse(code=404,"Unknown Error",null) }
fun serverError(): ErrorResponse {return ErrorResponse(code=505,"Server Error",null) }

fun exceptionError(e:Exception): ErrorResponse {return ErrorResponse(code=505,"Unexpected error: ${e.localizedMessage}", null)}

fun error(e:Exception): ErrorResponse {Log.e("Error",e.message?:"");return ErrorParser().errorResponse(e)}