package com.ossama.apps.managingstateexample.model.repository

import io.reactivex.Completable
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class Repository {

    // Possible responses of the asynchronous calls
    private val completableResponses = listOf(
        Completable.error(RuntimeException()),
        Completable.complete()
    )

    fun sendName(name: String?): Completable {
        return completableResponses
            .random()
            .delay(2, TimeUnit.SECONDS)
    }

    fun checkName(name: String?): Completable {
        return completableResponses
            .random()
            .delay(2, TimeUnit.SECONDS)
    }
}