package com.ossama.apps.managingstateexample.model.data

sealed class Result {

    sealed class SubmitResult : Result() {
        object Success: SubmitResult()
        object InFlight: SubmitResult()
        data class Failure(val errorMessage: String): SubmitResult()
    }

    sealed class CheckNameResult: Result() {
        object Success : CheckNameResult()
        object InFlight: CheckNameResult()
        data class Failure(val errorMessage: String): CheckNameResult()
    }
}
