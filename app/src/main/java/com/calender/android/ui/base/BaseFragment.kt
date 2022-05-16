package com.calender.android.ui.base

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


/**
 * Created by @mohamedebrahim96 on 16,May,2022
 * ShopiniWorld, Inc
 * ebrahimm131@gmail.com
 */
abstract class BaseFragment constructor(resId: Int) : Fragment(resId) {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(requireActivity(), resId)
    }
}