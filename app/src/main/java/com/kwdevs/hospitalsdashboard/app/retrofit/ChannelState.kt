package com.kwdevs.hospitalsdashboard.app.retrofit

sealed class ChannelState{
    data object UnSubscribed : ChannelState()
    data object Subscribed : ChannelState()
}