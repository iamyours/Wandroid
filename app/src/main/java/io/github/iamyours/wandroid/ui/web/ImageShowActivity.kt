package io.github.iamyours.wandroid.ui.web

import android.os.Bundle
import androidx.core.view.ViewCompat
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityImageShowBinding
import io.github.iamyours.wandroid.extension.displayCenterInside
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.extension.screenHeight
import io.github.iamyours.wandroid.extension.screenWidth
import io.github.iamyours.wandroid.util.Constants
import kotlinx.android.synthetic.main.activity_image_show.*

class ImageShowActivity : BaseActivity<ActivityImageShowBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_image_show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = Constants.sharedUrl
        val lp = image.layoutParams
        lp.width = screenWidth()
        lp.height = screenHeight()
        image.layoutParams = lp
        ViewCompat.setTransitionName(image, "image")
        image.displayCenterInside(url)
        image.setOnClickListener { onBackPressed() }
    }
}