package com.xxf.view.round

import android.R
import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Description: eg app:radius="4dp"
<!-- 定义四个角的半径。 -->
<attr name="radius" format="dimension" />
<!-- 左上角的半径。 -->
<attr name="topLeftRadius" format="dimension" />
<!-- 右上角的半径。 -->
<attr name="topRightRadius" format="dimension" />
<!-- 左下角的半径。 -->
<attr name="bottomLeftRadius" format="dimension" />
<!-- 右下角的半径。 -->
<attr name="bottomRightRadius" format="dimension" />

 * @Author: XGod
 * @CreateDate: 2018/6/25 15:49
 */
class XXFRoundCheckedImageView : AppCompatImageView, Checkable,XXFRoundWidget {
    private var mChecked = false

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ){
        CornerUtil.clipView(this, attrs)
    }


    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            //刷新drawable状态
            refreshDrawableState()
        }
    }

    /**
     * Imageview默认不支持state_checked状态，需要重写此方法
     *
     * @param extraSpace
     * @return
     */
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECK_STATE_SET)
        }
        return drawableState
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    companion object {
        private val CHECK_STATE_SET = intArrayOf(R.attr.state_checked)
    }

    override fun setRadius(radius: Float) {
        CornerUtil.clipViewRadius(this, radius)
    }
}
