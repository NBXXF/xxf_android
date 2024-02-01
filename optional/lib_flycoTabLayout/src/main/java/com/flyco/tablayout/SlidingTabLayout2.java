package com.flyco.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.transition.TabScaleTransformer;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** 滑动TabLayout,对于ViewPager2的依赖性强 */
/**
 * 属性中文
 * name	format	description
 * tl_indicator_color	color	设置显示器颜色
 * tl_indicator_height	dimension	设置显示器高度
 * tl_indicator_width	dimension	设置显示器固定宽度
 * tl_indicator_margin_left	dimension	设置显示器margin,当indicator_width大于0,无效
 * tl_indicator_margin_top	dimension	设置显示器margin,当indicator_width大于0,无效
 * tl_indicator_margin_right	dimension	设置显示器margin,当indicator_width大于0,无效
 * tl_indicator_margin_bottom	dimension	设置显示器margin,当indicator_width大于0,无效
 * tl_indicator_corner_radius	dimension	设置显示器圆角弧度
 * tl_indicator_gravity	enum	设置显示器上方(TOP)还是下方(BOTTOM),只对常规显示器有用
 * tl_indicator_style	enum	设置显示器为常规(NORMAL)或三角形(TRIANGLE)或背景色块(BLOCK)
 * tl_underline_color	color	设置下划线颜色
 * tl_underline_height	dimension	设置下划线高度
 * tl_underline_gravity	enum	设置下划线上方(TOP)还是下方(BOTTOM)
 * tl_divider_color	color	设置分割线颜色
 * tl_divider_width	dimension	设置分割线宽度
 * tl_divider_padding	dimension	设置分割线的paddingTop和paddingBottom
 * tl_tab_padding	dimension	设置tab的paddingLeft和paddingRight
 * tl_tab_space_equal	boolean	设置tab大小等分
 * tl_tab_width	dimension	设置tab固定大小
 * tl_textsize	dimension	设置字体大小
 * tl_textSelectColor	color	设置字体选中颜色
 * tl_textUnselectColor	color	设置字体未选中颜色
 * tl_textBold	boolean	设置字体加粗
 * tl_iconWidth	dimension	设置icon宽度(仅支持CommonTabLayout)
 * tl_iconHeight	dimension	设置icon高度(仅支持CommonTabLayout)
 * tl_iconVisible	boolean	设置icon是否可见(仅支持CommonTabLayout)
 * tl_iconGravity	enum	设置icon显示位置,对应Gravity中常量值,左上右下(仅支持CommonTabLayout)
 * tl_iconMargin	dimension	设置icon与文字间距(仅支持CommonTabLayout)
 * tl_indicator_anim_enable	boolean	设置显示器支持动画(only for CommonTabLayout)
 * tl_indicator_anim_duration	integer	设置显示器动画时间(only for CommonTabLayout)
 * tl_indicator_bounce_enable	boolean	设置显示器支持动画回弹效果(only for CommonTabLayout)
 * tl_indicator_width_equal_title	boolean	设置显示器与标题一样长(only for SlidingTabLayout)
 */
public class SlidingTabLayout2 extends SlidingTabLayoutBase {

  private ViewPager2 mViewPager;
  private boolean mViewPageScrolling = false;
  
  /**
   * 用于监听viewpager2变化然后做出改变
   */
  ViewPager2PageChangeCallBack vpListener = new ViewPager2PageChangeCallBack();

  public SlidingTabLayout2(Context context) {
    this(context, null, 0);
  }

  public SlidingTabLayout2(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlidingTabLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void registerVpListener(ViewPager2 vp) {
    //首先取消注册
    if (this.mViewPager != null) {
      this.mViewPager.unregisterOnPageChangeCallback(vpListener);
    }
    this.mViewPager = vp;
    this.mViewPager.registerOnPageChangeCallback(vpListener);
  }

  /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
  public void setViewPager(ViewPager2 vp, List<String> titles) {
    if (vp == null || vp.getAdapter() == null) {
      throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
    }

    if (titles == null || titles.size() == 0) {
      throw new IllegalStateException("Titles can not be EMPTY !");
    }

    if (titles.size() != vp.getAdapter().getItemCount()) {
      throw new IllegalStateException("Titles length must be the same as the page count !");
    }

    this.mViewPager = vp;
    mTitles = new ArrayList<>();
    mTitles.addAll(titles);

    registerVpListener(vp);
    notifyDataSetChanged();
  }

  public void setViewPager(ViewPager2 vp, String[] titles) {
    ArrayList<String> titleList = new ArrayList<>();
    Collections.addAll(titleList, titles);
    setViewPager(vp, titleList);
  }
  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager2 vp, List<String> titles, FragmentManager fragmentManager,FragmentActivity fa,
      List<Fragment> fragments) {

    if (vp == null) {
      throw new IllegalStateException("ViewPager can not be NULL !");
    }

    if (titles == null || titles.size() == 0) {
      throw new IllegalStateException("Titles can not be EMPTY !");
    }

    registerVpListener(vp);

    this.mViewPager.setAdapter(new InnerPagerAdapter(fragments, fragmentManager, fa.getLifecycle()));
    mTitles = new ArrayList<>();
    mTitles.addAll(titles);
    notifyDataSetChanged();
  }
  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager2 vp, List<String> titles, FragmentActivity fa,
      List<Fragment> fragments) {

    if (vp == null) {
      throw new IllegalStateException("ViewPager can not be NULL !");
    }

    if (titles == null || titles.size() == 0) {
      throw new IllegalStateException("Titles can not be EMPTY !");
    }

    registerVpListener(vp);
    this.mViewPager.setAdapter(new InnerPagerAdapter(fa, fragments, fa.getLifecycle()));
    mTitles = new ArrayList<>();
    mTitles.addAll(titles);
    notifyDataSetChanged();
  }

  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager2 vp, String[] titles, FragmentActivity fa,
      List<Fragment> fragments) {

    ArrayList<String> arrayList = new ArrayList<>();
    Collections.addAll(arrayList,titles);
    setViewPager(vp, arrayList, fa, fragments);
  }

  //setter and getter
  public void setCurrentTab(int currentTab) {
    this.mCurrentTab = currentTab;
    mViewPager.setCurrentItem(currentTab);
  }

  public void setCurrentTab(int currentTab, boolean smoothScroll) {
    this.mCurrentTab = currentTab;
    mViewPager.setCurrentItem(currentTab, smoothScroll);
  }

  @Override int getPageCount() {
    RecyclerView.Adapter adapter = mViewPager.getAdapter();
    if (adapter != null) {
      return adapter.getItemCount();
    }
    return 0;
  }

  @Override int getCurrentItem() {

    return mViewPager.getCurrentItem();
  }

  @Override void setCurrentItem(int position) {
    mViewPager.setCurrentItem(position);
  }

  @Override void setCurrentItem(int position, boolean smooth) {
    mViewPager.setCurrentItem(position, smooth);
  }

  /**
   * 用于监听viewpager2变化然后做出改变
   */
  private class ViewPager2PageChangeCallBack extends ViewPager2.OnPageChangeCallback {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      /**
       * position:当前View的位置
       * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
       */
      mCurrentTab = position;
      mCurrentPositionOffset = positionOffset;
      tabScaleTransformer.onPageScrolled(position, positionOffset, positionOffsetPixels);
      scrollToCurrentTab();
      invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (!mViewPageScrolling) {
                if (position != mCurrentTab) {
                    mCurrentTab = position;
                    mCurrentPositionOffset = 0;
                    tabScaleTransformer.onPageScrolled(position, 0, 0);
                    scrollToCurrentTab();
                    invalidate();
                }
        }
      updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
       if (state == 2) {
                mViewPageScrolling = true;
            } else {
                mViewPageScrolling = false;
            }
    }
  }

  class InnerPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

    List<Fragment> fragmentArrayList = new ArrayList<>();

    public InnerPagerAdapter(
        FragmentActivity fragmentManager,
        List<Fragment> fragmentArrayList) {
      super(fragmentManager);
      this.fragmentArrayList = fragmentArrayList;
    }

    public InnerPagerAdapter(
        FragmentActivity fragmentManager,
        List<Fragment> fragmentArrayList, Lifecycle lifecycle) {
      super(fragmentManager.getSupportFragmentManager(), lifecycle);

      this.fragmentArrayList = fragmentArrayList;
    }

    public InnerPagerAdapter( List<Fragment> fragmentArrayList,FragmentManager fragmentManager, Lifecycle lifecycle) {
      super(fragmentManager,lifecycle);
      this.fragmentArrayList = fragmentArrayList;

    }

    public Fragment createFragment(int position) {
      return fragmentArrayList.get(position);
    }

    @Override public int getItemCount() {
      return fragmentArrayList.size();
    }
  }
}
