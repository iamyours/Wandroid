package io.github.iamyours.wandroid.extension

import android.app.Activity
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> FragmentActivity.viewModel() =
    lazy { ViewModelProviders.of(this).get(T::class.java) }

inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline block: T.() -> Unit) =
    lazy {
        ViewModelProviders.of(this).get(T::class.java).apply(block)
    }

inline fun <reified T : ViewModel> Fragment.viewModel() =
    lazy { ViewModelProviders.of(this).get(T::class.java) }

/**
 * 相同类型使用多个的情况下
 */
@JvmOverloads
inline fun <reified T : ViewModel> Fragment.viewModel(
    key: Int? = null,
    crossinline block: T.() -> Unit
) =
    lazy {
        ViewModelProviders.of(this).get(key.toString(), T::class.java)
            .apply(block)
    }

/**
 * 获取参数
 */
inline fun <reified T> Activity.arg(key: String) =
    lazy {
        val ret = when (T::class.java) {
            String::class.java -> intent.getStringExtra(key)
            Int::class.java -> intent.getIntExtra(key, 0)
            Float::class.java -> intent.getFloatExtra(key, 0f)
            Boolean::class.java -> intent.getBooleanExtra(key, false)
            else -> (intent.getParcelableExtra(key) as Parcelable)
        }
        ret as T?
    }

inline fun <reified T> Fragment.arg(key: String) =
    lazy {
        val ret = when (T::class.java) {
            String::class.java -> arguments?.getString(key)
            Int::class.java -> arguments?.getInt(key)
            else -> null
        }
        ret as T?
    }



