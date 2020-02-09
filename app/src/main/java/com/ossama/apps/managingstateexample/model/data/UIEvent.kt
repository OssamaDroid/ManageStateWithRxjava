package com.ossama.apps.managingstateexample.model.data

sealed class UIEvent
data class SubmitEvent(val name: String? = null) : UIEvent()
data class CheckNameEvent(val name: String? = null) : UIEvent()