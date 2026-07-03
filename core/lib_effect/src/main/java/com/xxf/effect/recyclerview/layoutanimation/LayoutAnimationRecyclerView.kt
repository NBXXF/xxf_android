package com.xxf.effect.recyclerview.layoutanimation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager




/**
 * 修复 LayoutAnimationController$AnimationParameters cannot be cast to android.view.animation.GridLayoutAnimationController$AnimationParameters
 */
class LayoutAnimationRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun attachLayoutAnimationParameters(
        child: View,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        val layoutManager = this.layoutManager
        if (adapter != null && (layoutManager is GridLayoutManager
                    || layoutManager is StaggeredGridLayoutManager)
        ) {
            var animationParams =
                params.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters
            if (animationParams == null) {
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams
            }
            var columns = 0
            columns = if (layoutManager is GridLayoutManager) {
                (layoutManager as GridLayoutManager).spanCount
            } else {
                (layoutManager as StaggeredGridLayoutManager).spanCount
            }
            animationParams.count = count
            animationParams.index = index
            animationParams.columnsCount = columns
            animationParams.rowsCount = count / columns
            val invertedIndex = count - 1 - index
            animationParams.column = columns - 1 - invertedIndex % columns
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}