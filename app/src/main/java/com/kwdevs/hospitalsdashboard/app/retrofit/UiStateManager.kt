package com.kwdevs.hospitalsdashboard.app.retrofit

import androidx.compose.runtime.MutableState

@Suppress("unused")
class UiStateManager(val loading:MutableState<Boolean>,
                     private val fail:MutableState<Boolean>,
                     val success:MutableState<Boolean>,
                     private val empty:MutableState<Boolean>) {

    init {
        onCreate()
    }
    private fun onCreate(){loading.value=false;fail.value=false;success.value=false;empty.value=true}
    fun onLoading(){loading.value=true;success.value=false;empty.value=true;fail.value=true}
    fun onFail(){loading.value=false;success.value=false;empty.value=true;fail.value=true}
    fun onSuccess(){loading.value=false;success.value=true;fail.value=false}
    fun onEmpty(){empty.value=true}
    fun onNotEmpty(){empty.value=false}
    fun isLoading(): Boolean {return if(!success.value && !fail.value){loading.value}else{false}}
    fun isSuccess(): Boolean {return if(!loading.value && !fail.value){success.value}else{false}}
    fun isFail(): Boolean {return if(!loading.value && !success.value){fail.value}else{false}}
    fun isEmpty(): Boolean {return empty.value}
}