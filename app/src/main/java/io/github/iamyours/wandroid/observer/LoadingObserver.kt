package io.github.iamyours.wandroid.observer

import android.content.Context
import androidx.lifecycle.Observer
import com.kaopiz.kprogresshud.KProgressHUD

class LoadingObserver(context: Context) : Observer<Boolean> {
    private val dialog = KProgressHUD(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setCancellable(false)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)

    override fun onChanged(show: Boolean?) {
        if (show == null) return
        if (show) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}