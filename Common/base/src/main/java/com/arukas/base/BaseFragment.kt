package com.arukas.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<BD:ViewBinding,VM:BaseViewModel>: Fragment() {
    protected abstract val binding: BD
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initArguments(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onDestroyView() {
        destroyView()
        super.onDestroyView()
    }

    override fun onPause() {
        pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    open fun initArguments(arguments: Bundle?) {}

    open fun resume(){}

    open fun pause() {}

    open fun destroyView(){}

    abstract fun initView()
}