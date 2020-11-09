package com.example.fitnessapp.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


open class ExpandAnimation(
    private val view: View,
    private val originalHeight: Int,
    toHeight: Int,
    private val originalWidth: Int,
    toWidth: Int
) :
    Animation() {
    private var perValueH: Float = (toHeight - originalHeight).toFloat()
    private var perValueW: Float = (toWidth - originalWidth).toFloat()
    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation
    ) {
        view.layoutParams.height = (originalHeight + perValueH * interpolatedTime).toInt()
        view.layoutParams.width = (originalWidth + perValueW * interpolatedTime).toInt()
        view.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

}