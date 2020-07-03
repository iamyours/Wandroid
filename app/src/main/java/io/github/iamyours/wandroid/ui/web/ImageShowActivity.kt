package io.github.iamyours.wandroid.ui.web

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityImageShowBinding
import io.github.iamyours.wandroid.extension.displayCenterInside
import io.github.iamyours.wandroid.extension.displayWhenLayout
import io.github.iamyours.wandroid.util.Constants
import kotlinx.android.synthetic.main.activity_image_show.*


class ImageShowActivity : BaseActivity<ActivityImageShowBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_image_show
    var lw = 0
    var lh = 0
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ll.background.alpha = 255
        ViewCompat.setTransitionName(image, "image")

        image.setOnClickListener { onBackPressed() }
        val url = Constants.sharedUrl

        ll.displayWhenLayout {
            lw = ll.width
            lh = ll.height
            image.displayCenterInside(
                url,
                ll.width.toFloat(),
                ll.height.toFloat()
            )
        }
        iv_screen.setOnClickListener {
            rotationImage()
        }
    }

    private fun rotationImage() {
        flag = !flag
        if (flag) {
            val lp = image.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = lh
            lp.height = lw
            lp.leftMargin = -(lh - lw) / 2
            lp.topMargin = (lh - lw) / 2
            image.layoutParams = lp
            val p3 =
                PropertyValuesHolder.ofFloat("rotation", 0f, -90f)
            val anim = ObjectAnimator.ofPropertyValuesHolder(image, p3)
                .setDuration(300)
            anim.addListener(onEnd = {
                image.setImageDrawableToWith(null, lh.toFloat(), lw.toFloat())
            })

            anim.start()
        } else {
            val lp = image.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = lw
            lp.height = lh
            lp.leftMargin = 0
            lp.topMargin = 0
            image.layoutParams = lp
            val p3 =
                PropertyValuesHolder.ofFloat("rotation", -90f, 0f)
            val anim = ObjectAnimator.ofPropertyValuesHolder(image, p3)
                .setDuration(300)
            anim.addListener(onEnd = {
                image.setImageDrawableToWith(null, lw.toFloat(), lh.toFloat())
            })
            anim.start()
        }
    }

}