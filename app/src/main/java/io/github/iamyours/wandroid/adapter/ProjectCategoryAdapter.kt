package io.github.iamyours.wandroid.adapter

import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemProjectCategoryBinding
import io.github.iamyours.wandroid.vo.ProjectCategoryVO

class ProjectCategoryAdapter(var selectItem: MutableLiveData<ProjectCategoryVO>) :
    DataBoundAdapter<ProjectCategoryVO, ItemProjectCategoryBinding>() {
    override fun initView(
        binding: ItemProjectCategoryBinding,
        item: ProjectCategoryVO
    ) {
        binding.vo = item
        binding.root.setOnClickListener {
            updateSelectItem(item)
        }
    }

    fun updateSelectItem(item: ProjectCategoryVO) {
        mData.forEach { it.select = false }
        item.select = true
        notifyDataSetChanged()
        selectItem.value = item
    }

    override val layoutId: Int
        get() = R.layout.item_project_category

}