package com.ossama.apps.managingstateexample

import com.ossama.apps.managingstateexample.model.repository.Repository
import com.ossama.apps.managingstateexample.viewModel.ViewModelFactory

class AppContainer {

    private val repository = Repository()

    val viewModelFactory = ViewModelFactory(repository)
}