package com.ossama.apps.managingstateexample.model.data

sealed class Action
data class SubmitNameAction(val name: String? = null) : Action()
data class CheckNameAction(val name: String? = null) : Action()