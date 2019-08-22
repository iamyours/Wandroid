package io.github.iamyours.wandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.observer.LoadingObserver

open abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    abstract val layoutId: Int
    lateinit var binding: T
    val loadingState = MutableLiveData<Boolean>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingState.observe(this, LoadingObserver(activity!!))
    }
}