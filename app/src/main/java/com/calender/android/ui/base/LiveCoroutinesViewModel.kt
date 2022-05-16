package com.calender.android.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers


/**
 * Created by @mohamedebrahim96 on 16,May,2022
 * ShopiniWorld, Inc
 * ebrahimm131@gmail.com
 */
abstract class LiveCoroutinesViewModel : ViewModel() {

    inline fun <T> launchOnViewModelScope(crossinline block: suspend () -> LiveData<T>): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(block())
        }
    }
}