package com.ossama.apps.managingstateexample.viewModel

import com.ossama.apps.managingstateexample.model.repository.Repository

interface Factory<T> {
    fun get(): T
}

class ViewModelFactory(private val repository: Repository) : Factory<ViewModel> {

    private val viewModel by lazy { ViewModel(repository) }

    override fun get(): ViewModel {
        return viewModel
    }
}