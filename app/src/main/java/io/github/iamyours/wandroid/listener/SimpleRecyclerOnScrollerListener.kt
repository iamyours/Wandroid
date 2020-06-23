package io.github.iamyours.wandroid.listener

import android.widget.BaseAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.iamyours.wandroid.adapter.DataBoundAdapter
import io.github.iamyours.wandroid.base.BaseActivity

open class SimpleRecyclerOnScrollerListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as LinearLayoutManager
        val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
        val itemCount = manager.itemCount
        val first = manager.findFirstVisibleItemPosition()
        val view = manager.findViewByPosition(first)
        val top = view?.top ?: 0
        /**
         * 是否滑动到顶部
         */
        onScrollUp(first == 0 && top == 0)
        scrollToPosition(first)
        val adapter = recyclerView.adapter
        if (adapter is DataBoundAdapter<*, *>) {
            adapter.getData()[first]?.let { onFirstObject(it) }
        }
        // 判断是否滑动到了最后一个item，并且是向上滑动
        if (lastItemPosition == itemCount - 1 && dy > 0) {
            // 加载更多
            onLoadMore()
        }
    }

    /**
     * 加载更多回调
     */
    open fun onLoadMore() {}

    /**
     * 向上滑动回调
     */
    open fun onScrollUp(isTop: Boolean) {}

    /**
     * 滑动到某位置
     */
    open fun scrollToPosition(position: Int) {}

    /**
     * 第一项对应的item
     */
    open fun onFirstObject(obj: Any) {}
}