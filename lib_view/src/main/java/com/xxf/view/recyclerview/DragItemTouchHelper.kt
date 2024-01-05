package com.xxf.view.recyclerview

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xxf.ktx.identityId
import com.xxf.ktx.standard.runIf
import com.xxf.ktx.vibrator
import java.util.Collections

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description recyclerview 拖动 排序 工具类
 * 用法 DragItemTouchHelper().attachToRecyclerView(xx)
 */
class DragItemTouchHelper : ItemTouchHelper.Callback {
    /**
     * 拖动中的view 的样式
     * -1.0f 代表不操作
     */
    data class DraggingViewStyle(
        val backgroundColor: Int = Color.RED,
        val elevation: Float = -1.0f,
        val alpha: Float = -1.0f
    )

    private data class DraggingViewStyleCache(
        val background: Drawable,
        val elevation: Float,
        val alpha: Float
    ) {
        constructor(view: View) : this(view.background, view.elevation, view.alpha)
    }

    /**
     * 数据源提供
     */
    interface AdapterSourceProvider {
        fun getAdapterSource(): List<*>
    }

    private val draggingViewStyleCacheMap: MutableMap<String, DraggingViewStyleCache> = HashMap()
    private var adapterSourceProvider: AdapterSourceProvider
    private val draggingViewStyle: DraggingViewStyle;

    constructor(adapterSourceProvider: AdapterSourceProvider) {
        this.adapterSourceProvider = adapterSourceProvider
        this.draggingViewStyle = DraggingViewStyle()
    }

    constructor(
        adapterSourceProvider: AdapterSourceProvider,
        draggingViewStyle: DraggingViewStyle
    ) {
        this.adapterSourceProvider = adapterSourceProvider
        this.draggingViewStyle = draggingViewStyle;
    }

    fun attachToRecyclerView(recyclerView: RecyclerView): ItemTouchHelper {
        return ItemTouchHelper(this)
            .apply { attachToRecyclerView(recyclerView) }
    }

    //开启长按拖拽功能，默认为true【暂时用不到】
    //如果需要我们自定义拖拽和滑动，可以设置为false，然后调用itemTouchHelper.startDrag(ViewHolder)方法来开启！
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    //开始滑动功能，默认为true【暂时用不到】
    //如果需要我们自定义拖拽和滑动，可以设置为false，然后调用itemTouchHelper.startSwipe(ViewHolder)方法来开启！
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * 用于设置是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
     * 比如如果是列表类型的RecyclerView，拖拽只有UP、DOWN两个方向
     * 而如果是网格类型的则有UP、DOWN、LEFT、RIGHT四个方向
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //swipeFlags是侧滑标志，我们把swipeFlags 都设置为0，表示不处理滑动操作
        //dragFlags 是拖拽标志
        val dragFlags =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 如果我们设置了非0的dragFlags ，那么当我们长按item的时候就会进入拖拽并在拖拽过程中不断回调onMove()方法
     * 我们就在这个方法里获取当前拖拽的item和已经被拖拽到所处位置的item的ViewHolder，
     * 有了这2个ViewHolder，我们就可以交换他们的数据集并调用Adapter的notifyItemMoved方法来刷新item
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //得到拖动ViewHolder的position
        val fromPosition = viewHolder.bindingAdapterPosition
        //得到目标ViewHolder的position
        val toPosition = target.bindingAdapterPosition
        //这里可以添加判断，实现某一项不可交换
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(adapterSourceProvider.getAdapterSource(), i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(adapterSourceProvider.getAdapterSource(), i, i - 1)
            }
        }
        recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    /**
     * 同理如果我们设置了非0的swipeFlags，我们在侧滑item的时候就会回调onSwiped的方法，我们不处理这个事件，空着就行了。
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    /**
     * //我们希望拖拽的Item在拖拽的过程中发生震动或者颜色变深，这样就需要继续重写下面两个方法
     * //当长按选中item的时候（拖拽开始的时候）调用
     * //ACTION_STATE_IDLE：闲置状态
     * //ACTION_STATE_SWIPE：滑动状态
     * //ACTION_STATE_DRAG：拖拽状态
     *
     * @param viewHolder
     * @param actionState
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            try {
                viewHolder!!.itemView.context.vibrator?.vibrate(70)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            viewHolder?.let {
                //记录拖动前的样式
                draggingViewStyleCacheMap[viewHolder.itemView.identityId] =
                    DraggingViewStyleCache(viewHolder.itemView)

                //初始化拖动中的样式
                with(viewHolder.itemView) {
                    this.isPressed = true
                    draggingViewStyle.backgroundColor.runIf({ it >= 0 }) {
                        this.background = ColorDrawable(it)
                    }
                    draggingViewStyle.alpha.runIf({ it >= 0 }) {
                        this.alpha = it
                    }
                    draggingViewStyle.elevation.runIf({ it >= 0 }) {
                        this.elevation = it
                    }
                }
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 当手指松开的时候（拖拽或滑动完成的时候）调用，这时候我们可以将item恢复为原来的状态（相对于背景颜色加深来说的）
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        //恢复样式
        val dragViewStyleCache = draggingViewStyleCacheMap[viewHolder.itemView.identityId]
        dragViewStyleCache?.let {
            with(viewHolder.itemView) {
                this.isPressed = false
                this.background = it.background
                this.alpha = it.alpha
                this.elevation = it.elevation
            }
            draggingViewStyleCacheMap.remove(viewHolder.itemView.identityId)
        }

        //解决重叠问题
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}