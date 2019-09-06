package io.github.iamyours.wandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fastloan.app.ui.adapter.DataBoundViewHolder

open abstract class DataBoundAdapter<T, V : ViewDataBinding> :
    RecyclerView.Adapter<DataBoundViewHolder<V>>() {
    abstract val layoutId: Int
    protected val mData = ArrayList<T>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DataBoundViewHolder<V> {
        return DataBoundViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<V>,
        position: Int
    ) {
        initView(holder.binding, mData[position])
        holder.binding.executePendingBindings()//必须调用，否则闪屏
    }

    abstract fun initView(binding: V, item: T)

    fun addAll(list: List<T>, isFirst: Boolean) {
        if (isFirst) mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(): List<T> {
        return mData
    }
}