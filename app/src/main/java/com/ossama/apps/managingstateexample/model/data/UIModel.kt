package com.ossama.apps.managingstateexample.model.data

object UIModel {

    var inProgress: Boolean = false
        private set

    var success: Boolean = false
        private set

    var errorMessage: String? = null
        private set

    fun idle(): UIModel {
        inProgress = false
        success = false
        errorMessage = null
        return this
    }

    fun inProgress(): UIModel {
        inProgress = true
        success = false
        errorMessage = null
        return this
    }

    fun success(): UIModel {
        inProgress = false
        success = true
        errorMessage = null
        return this
    }

    fun error(errorMessage: String): UIModel {
        inProgress = false
        success = false
        UIModel.errorMessage = errorMessage
        return this
    }

}