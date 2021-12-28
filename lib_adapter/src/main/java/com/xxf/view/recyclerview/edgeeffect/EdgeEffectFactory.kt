package com.xxf.view.recyclerview.adapter

import android.view.View
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 用法
 * 1.adapter的viewHolder 实现IEdgeEffectViewHolder  默认实现类:EdgeSpringEffectViewHolder
 * 2.recyclerView.edgeEffectFactory = EdgeEffectFactory() 默认实现类：EdgeSpringEffectFactory
 */
/**
 * 拖动边界效果
 */
interface IEdgeEffectViewHolder<T : DynamicAnimation<T>> {
    /**
     * 边界动画
     */
    fun getEdgeEffectAnimation(): DynamicAnimation<T>;
}

/**
 * 弹簧效果适配器
 */
open class EdgeSpringEffectViewHolder<V : ViewBinding, T> : XXFViewHolder<V, T>,
    IEdgeEffectViewHolder<SpringAnimation> {
    constructor(baseAdapter: BaseAdapter<V, T>?, binding: V, bindItemClick: Boolean) : super(
        baseAdapter,
        binding,
        bindItemClick
    )

    constructor(baseAdapter: BaseAdapter<V, T>?, itemView: View?, bindItemClick: Boolean) : super(
        baseAdapter,
        itemView,
        bindItemClick
    )

    private val mTranslationEdgeAnimY: SpringAnimation by lazy {
        SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
    }
    private val mTranslationEdgeAnimX: SpringAnimation by lazy {
        SpringAnimation(itemView, SpringAnimation.TRANSLATION_X)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
    }

    override fun getEdgeEffectAnimation(): DynamicAnimation<SpringAnimation> {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            return when (layoutManager.orientation) {
                RecyclerView.HORIZONTAL -> mTranslationEdgeAnimX
                else -> mTranslationEdgeAnimY
            }
        } else {
            return mTranslationEdgeAnimY
        }
    }
}

/**
 * 弹簧效果
 */
open class EdgeSpringEffectFactory : RecyclerView.EdgeEffectFactory {
    private var overscrollTranslationMagnitude: Float
    private var flingTranslationMagnitude: Float

    constructor(
        overscrollTranslationMagnitude: Float = 0.5f,
        flingTranslationMagnitude: Float = 0.5f
    ) : super() {
        this.overscrollTranslationMagnitude = overscrollTranslationMagnitude
        this.flingTranslationMagnitude = flingTranslationMagnitude
    }


    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(view.context) {
            override fun onPull(deltaDistance: Float) {
                //去掉边界颜色
                //super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                // 去掉边界颜色
                // super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            private fun handlePull(deltaDistance: Float) {
                if (direction == DIRECTION_LEFT || direction == DIRECTION_RIGHT) {
                    val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                    val translationXDelta =
                        sign * view.width * deltaDistance * overscrollTranslationMagnitude
                    view.forEachVisibleHolder { holder: RecyclerView.ViewHolder ->
                        if (holder is IEdgeEffectViewHolder<*>) {
                            holder.getEdgeEffectAnimation().cancel()
                            holder.itemView.translationX += translationXDelta
                        }
                    }
                } else {
                    val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                    val translationYDelta =
                        sign * view.height * deltaDistance * overscrollTranslationMagnitude
                    view.forEachVisibleHolder { holder: RecyclerView.ViewHolder ->
                        if (holder is IEdgeEffectViewHolder<*>) {
                            holder.getEdgeEffectAnimation().cancel()
                            holder.itemView.translationY += translationYDelta
                        }
                    }
                }
            }

            override fun onRelease() {
                //  super.onRelease()
                view.forEachVisibleHolder { holder: RecyclerView.ViewHolder ->
                    if (holder is IEdgeEffectViewHolder<*>) {
                        holder.getEdgeEffectAnimation().start()
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                //   super.onAbsorb(velocity)
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationVelocity = sign * velocity * flingTranslationMagnitude
                view.forEachVisibleHolder { holder: RecyclerView.ViewHolder ->
                    if (holder is IEdgeEffectViewHolder<*>) {
                        holder.getEdgeEffectAnimation().setStartVelocity(translationVelocity)
                            .start()
                    }
                }
            }
        }
    }


    /**
     * Runs [action] on every visible [RecyclerView.ViewHolder] in this [RecyclerView].
     */
    private inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
        action: (T) -> Unit
    ) {
        for (i in 0 until childCount) {
            action(getChildViewHolder(getChildAt(i)) as T)
        }
    }
}
