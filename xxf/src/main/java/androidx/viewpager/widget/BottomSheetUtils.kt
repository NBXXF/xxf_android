package androidx.viewpager.widget

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.xxf.arch.R

/**
 * 用法: 先加layout_behavior
 *     <androidx.viewpager.widget.ViewPager
android:id="@+id/vp"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:background="@color/grey_db"
app:layout_behavior="androidx.viewpager.widget.ViewPagerBottomSheetBehavior" />


<androidx.viewpager2.widget.ViewPager2
android:id="@+id/vp"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:background="@color/grey_db"
app:layout_behavior="androidx.viewpager.widget.ViewPagerBottomSheetBehavior" />

 */
object BottomSheetUtils {

    /**
     * 是否是在bottomSheet容器中
     */
    fun isInBottomSheetLayout(view: View?): Boolean {
        var current = view;
        while (current != null) {
            if (current.id == R.id.design_bottom_sheet) {
                return true
            }
            current = current.parent as? View
        }
        return false
    }

    fun setupViewPager(vp: ViewPager) {
        findBottomSheetParent(vp)?.also {
            vp.addOnPageChangeListener(
                BottomSheetViewPagerListener(
                    vp, ViewPagerBottomSheetBehavior.from(it)
                )
            )
        }
    }

    @JvmStatic
    fun getCurrentViewWithVP(vp: ViewPager): View? {
        val currentItem: Int = vp.currentItem
        (0 until vp.childCount).forEach { index ->
            val child = vp.getChildAt(index)
            val layoutParams = child.layoutParams as ViewPager.LayoutParams
            layoutParams.position
            val position = layoutParams.position
            if (layoutParams.isDecor.not() && currentItem == position) {
                return child
            }
        }
        return null
    }

    private fun findBottomSheetParent(view: View): View? {
        var current: View? = view
        while (current != null) {
            val params = current.layoutParams
            if (params is CoordinatorLayout.LayoutParams && params.behavior is ViewPagerBottomSheetBehavior<*>) {
                return current
            }
            val parent = current.parent
            current = if (parent !is View) null else parent
        }
        return null
    }

    private class BottomSheetViewPagerListener(
        private val vp: ViewPager,
        private val behavior: ViewPagerBottomSheetBehavior<View>
    ) : SimpleOnPageChangeListener() {

        override fun onPageSelected(position: Int) {
            vp.post(behavior::invalidateScrollingChild)
        }

    }


    fun setupViewPager(vp: ViewPager2) {
        findBottomSheetParent(vp)?.also {
            vp.registerOnPageChangeCallback(
                BottomSheetViewPagerListener2(
                    vp,
                    ViewPagerBottomSheetBehavior.from(it)
                )
            )
        }
    }

    private class BottomSheetViewPagerListener2(
        private val vp: ViewPager2,
        private val behavior: ViewPagerBottomSheetBehavior<View>
    ) : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            vp.post(behavior::invalidateScrollingChild)
        }
    }

}