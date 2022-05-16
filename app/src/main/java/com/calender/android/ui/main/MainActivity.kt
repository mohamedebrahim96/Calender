package com.calender.android.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.calender.android.R
import com.calender.android.databinding.ActivityMainBinding
import com.calender.android.ui.base.DataBindingActivity
import com.calender.android.ui.home.HomeFragment
import com.calender.android.utils.setupFragment.setupFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivity(){

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        setupFragment(
            this,
            HomeFragment()
        )
    }
}