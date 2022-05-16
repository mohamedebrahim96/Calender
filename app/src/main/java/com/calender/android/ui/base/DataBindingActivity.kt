package com.calender.android.ui.base

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class DataBindingActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy {
        supportActionBar?.hide()
        DataBindingUtil.setContentView<T>(this, resId)
    }
}
