package io.github.iamyours.wandroid.binds

import androidx.databinding.BindingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

@BindingAdapter(value = ["refreshing", "moreLoading", "hasMore"], requireAll = false)
fun bindSmartRefreshLayout(
    smartLayout: SmartRefreshLayout,
    refreshing: Boolean,
    moreLoading: Boolean,
    hasMore: Boolean

) {
    if (!refreshing) smartLayout.finishRefresh()
    if (!moreLoading) smartLayout.finishLoadMore()
    smartLayout.setEnableLoadMore(hasMore)
}

@BindingAdapter(value = ["onRefreshListener", "onLoadMoreListener"], requireAll = false)
fun bindListener(
    smartLayout: SmartRefreshLayout,
    refreshListener: OnRefreshListener?,
    loadMoreListener: OnLoadMoreListener?
) {
    smartLayout.setOnRefreshListener(refreshListener)
    smartLayout.setOnLoadMoreListener(loadMoreListener)
}



