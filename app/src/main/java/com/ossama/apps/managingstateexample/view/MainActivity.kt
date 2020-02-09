package com.ossama.apps.managingstateexample.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.ossama.apps.managingstateexample.MyApplication
import com.ossama.apps.managingstateexample.R
import com.ossama.apps.managingstateexample.viewModel.ViewModel
import com.ossama.apps.managingstateexample.model.data.CheckNameEvent
import com.ossama.apps.managingstateexample.model.data.SubmitEvent
import com.ossama.apps.managingstateexample.model.data.UIModel
import com.ossama.apps.managingstateexample.model.data.UIEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init the ViewModel
        val appContainer = (application as MyApplication).appContainer
        viewModel = appContainer.viewModelFactory.get()

        setContentView(R.layout.activity_main)

        // Create Observable for button clicks
        val submitEvents: Observable<SubmitEvent> =
            submit_btn.clicks()
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { SubmitEvent(edit_text.text.toString()) }

        // Create Observable for text typing
        val checkNameEvents: Observable<CheckNameEvent> =
            edit_text.afterTextChangeEvents()
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { CheckNameEvent(it.editable?.toString()) }

        // Merged events
        val events: Observable<UIEvent> = Observable.merge(submitEvents, checkNameEvents)

        compositeDisposable.add(
            events
                .compose(viewModel.uiEventsTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { uiModel ->
                    // Set the UI accordingly
                    submit_btn.isEnabled = !uiModel.inProgress
                    progress_bar.visibility = if (uiModel.inProgress) View.VISIBLE else View.GONE
                    setRequestStatus(uiModel)
                }
        )
    }

    // Set the status of the sent out requests from the resulting UIModel
    private fun setRequestStatus(uiModel: UIModel) {
        when {
            uiModel.success -> {
                requests_status.visibility = View.VISIBLE
                requests_status.text = getString(R.string.success)
            }
            uiModel.errorMessage != null -> {
                requests_status.visibility = View.VISIBLE
                requests_status.text = getString(R.string.failure)
            }
            else -> requests_status.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
