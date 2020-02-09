package com.ossama.apps.managingstateexample.viewModel

import com.ossama.apps.managingstateexample.model.data.*
import com.ossama.apps.managingstateexample.model.data.Result.CheckNameResult
import com.ossama.apps.managingstateexample.model.data.Result.SubmitResult
import com.ossama.apps.managingstateexample.model.repository.Repository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

class ViewModel(private val repository: Repository) {

    val uiEventsTransformer: ObservableTransformer<UIEvent, UIModel> =
        ObservableTransformer { uiEvents ->
            uiEvents.publish {
                Observable.merge(
                    it.ofType(SubmitEvent::class.java)
                        .compose(transformSubmitNameEventToAction)
                        .compose(submitName),
                    it.ofType(CheckNameEvent::class.java)
                        .compose(transformCheckNameEventToAction)
                        .compose(checkName)
                )
                    .scan(UIModel.idle(), { _, result ->
                        when (result) {
                            SubmitResult.InFlight, CheckNameResult.InFlight -> UIModel.inProgress()
                            SubmitResult.Success, CheckNameResult.Success -> UIModel.success()
                            is SubmitResult.Failure -> UIModel.error(result.errorMessage)
                            is CheckNameResult.Failure -> UIModel.error(result.errorMessage)
                        }
                    })
            }
        }

    private val transformSubmitNameEventToAction: ObservableTransformer<SubmitEvent, SubmitNameAction> =
        ObservableTransformer { events ->
            events.map { SubmitNameAction(it.name) }
        }

    private val transformCheckNameEventToAction: ObservableTransformer<CheckNameEvent, CheckNameAction> =
        ObservableTransformer { events ->
            events.map { CheckNameAction(it.name) }
        }

    private val submitName: ObservableTransformer<SubmitNameAction, SubmitResult> =
        ObservableTransformer { actions ->
            actions.flatMap { event ->
                repository.sendName(event.name)
                    .andThen(Observable.just(SubmitResult.Success as SubmitResult))
                    .onErrorReturn { SubmitResult.Failure(it.message ?: "Your error message") }
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(SubmitResult.InFlight)
            }
        }

    private val checkName: ObservableTransformer<CheckNameAction, CheckNameResult> =
        ObservableTransformer { actions ->
            actions.switchMap { event ->
                repository.checkName(event.name)
                    .andThen(Observable.just(CheckNameResult.Success as CheckNameResult))
                    .onErrorReturn { CheckNameResult.Failure(it.message ?: "") }
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(CheckNameResult.InFlight)
            }
        }
}