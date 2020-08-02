package com.rohit2810.leo_kotlin.ui.intro


import android.view.View
import androidx.viewpager.widget.ViewPager
import java.util.*


/**
 * Parallax transformer for ViewPagers that let you set different parallax
 * effects for each view in your Fragments.
 *
 * Created by Marcos Trujillo (#^.^#) on 1/10/14.
 */
class ParallaxPageTransformer : ViewPager.PageTransformer {
    private var mViewsToParallax: MutableList<ParallaxTransformInformation>? =
        ArrayList()

    constructor() {}
    constructor(viewsToParallax: MutableList<ParallaxTransformInformation>) {
        mViewsToParallax = viewsToParallax
    }

    fun addViewToParallax(
        viewInfo: ParallaxTransformInformation
    ): ParallaxPageTransformer {
        if (mViewsToParallax != null) {
            mViewsToParallax!!.add(viewInfo)
        }
        return this
    }

    override fun transformPage(
        view: View,
        position: Float
    ) {
        val pageWidth = view.width
        if (position < -1) {
            // This page is way off-screen to the left.
            view.alpha = 1f
        } else if (position <= 1 && mViewsToParallax != null) { // [-1,1]
            for (parallaxTransformInformation in mViewsToParallax!!) {
                applyParallaxEffect(
                    view, position, pageWidth, parallaxTransformInformation,
                    position > 0
                )
            }
        } else {
            // This page is way off-screen to the right.
            view.alpha = 1f
        }
    }

    private fun applyParallaxEffect(
        view: View, position: Float, pageWidth: Int,
        information: ParallaxTransformInformation, isEnter: Boolean
    ) {
        if (information.isValid && view.findViewById<View?>(information.resource) != null) {
            if (isEnter && !information.isEnterDefault) {
                view.findViewById<View>(information.resource).translationX =
                    -position * (pageWidth / information.parallaxEnterEffect)
            } else if (!isEnter && !information.isExitDefault) {
                view.findViewById<View>(information.resource).translationX =
                    -position * (pageWidth / information.parallaxExitEffect)
            }
        }
    }

    /**
     * Information to make the parallax effect in a concrete view.
     *
     * parallaxEffect positive values reduces the speed of the view in the translation
     * ParallaxEffect negative values increase the speed of the view in the translation
     * Try values to see the different effects. I recommend 2, 0.75 and 0.5
     */
    class ParallaxTransformInformation(
        resource: Int, parallaxEnterEffect: Float,
        parallaxExitEffect: Float
    ) {
        var resource = -1
        var parallaxEnterEffect = 1f
        var parallaxExitEffect = 1f
        val isValid: Boolean
            get() = parallaxEnterEffect != 0f && parallaxExitEffect != 0f && resource != -1

        val isEnterDefault: Boolean
            get() = parallaxEnterEffect == PARALLAX_EFFECT_DEFAULT

        val isExitDefault: Boolean
            get() = parallaxExitEffect == PARALLAX_EFFECT_DEFAULT

        companion object {
            const val PARALLAX_EFFECT_DEFAULT = -101.1986f
        }

        init {
            this.resource = resource
            this.parallaxEnterEffect = parallaxEnterEffect
            this.parallaxExitEffect = parallaxExitEffect
        }
    }
}