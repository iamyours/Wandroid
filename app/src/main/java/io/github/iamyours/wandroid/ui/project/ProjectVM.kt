package io.github.iamyours.wandroid.ui.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.vo.ProjectCategoryVO

class ProjectVM : BaseViewModel() {
    val selectCategory = MutableLiveData<ProjectCategoryVO>()//选中项目分类

    private var projectTree = Transformations.switchMap(refreshTrigger) {
        api.projectTree()
    }
    var categorys = Transformations.map(projectTree) {
        val list = it.data ?: ArrayList()
        if (list.isNotEmpty()) {
            selectCategory.value = list[0]
            list[0].select = true
        }
        list.forEachIndexed { index, vo ->
            vo.icon = Constants.categoryIcons[index]
        }
        list
    }

    init {
        selectCategory.observeForever {
            //每次选择重新刷新数据
            autoRefresh()
        }
    }

    private var _projectLit = Transformations.switchMap(page) {
        api.projectList(it, selectCategory.value?.id ?: 0)
    }

    var projectPage = mapPage(_projectLit)

    /**
     * 首次加载
     */
    override fun loadData() {
        refreshTrigger.value = true
    }

    /**
     * 分页刷新
     */
    override fun refresh() {
        page.value = 1
    }
}