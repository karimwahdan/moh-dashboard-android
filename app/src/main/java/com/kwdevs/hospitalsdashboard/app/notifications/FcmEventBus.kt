package com.kwdevs.hospitalsdashboard.app.notifications

import kotlinx.coroutines.flow.MutableSharedFlow

object FcmEventBus { val icuOfferMessages = MutableSharedFlow<NotificationModel?>(replay = 1) }
object ViewModelProvider { val fcmViewModel = FcmViewModel() }