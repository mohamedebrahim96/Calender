package com.calender.android.utils

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.calender.android.R


/**
 * Created by @mohamedebrahim96 on 16,May,2022
 * ShopiniWorld, Inc
 * ebrahimm131@gmail.com
 */
object setupFragment {

    fun setupFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        bundle: Bundle? = null,
        view: View? = null,
        tag: String? = null
    ) {
        val containerViewId =
            if (view == null) R.id.fragment_home_container else (view.parent as ViewGroup).id
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        if (bundle != null)
            fragment.arguments = bundle
        fragmentTransaction.replace(containerViewId, fragment)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }
}