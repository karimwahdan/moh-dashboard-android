package com.kwdevs.hospitalsdashboard.app.retrofit

sealed class ChannelType{
    data object Public : ChannelType()
    data object Private : ChannelType()
    data object Presence : ChannelType()
    data object Encrypted : ChannelType()

}