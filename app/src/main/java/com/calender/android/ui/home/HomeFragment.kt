package com.calender.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.calender.android.R
import com.calender.android.databinding.FragmentHomeBinding
import com.calender.android.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Created by @mohamedebrahim96 on 16,May,2022
 * ShopiniWorld, Inc
 * ebrahimm131@gmail.com
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home), View.OnClickListener {

    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initViews()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initViews() {
        binding.intercomFAB.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.intercomFAB -> {
                viewModel
            }
        }
    }

}