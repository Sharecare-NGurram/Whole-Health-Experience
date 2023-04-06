package com.carelon.whe.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.carelon.whe.domain.model.ErrorItem

interface BaseErrorViewModelProvider : LifecycleOwner {

    fun observeErrorModel(viewModel: BaseViewModel?) {
        viewModel?.errorViewLiveData?.observe(
            this,
            Observer { errorItem ->
                errorItem?.let {
                    showErrorDialog(it)
                }
            }
        )
    }

    fun showErrorDialog(errorItem: ErrorItem)
}