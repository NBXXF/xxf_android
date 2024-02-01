package com.flyco.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 滑动TabLayout,对于ViewPager的依赖性强 */

/**
 * 请使用viewPager2
 *
 */
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

public class SlidingTabLayout extends SlidingTabLayoutBase
    implements ViewPager.OnPageChangeListener {
  private ViewPager mViewPager;

  public SlidingTabLayout(Context context) {
    super(context);
  }

  public SlidingTabLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setViewPager(ViewPager vp, List<String> titles) {

    if (vp == null || vp.getAdapter() == null) {
      throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
    }

    if (titles == null || titles.size() == 0) {
      throw new IllegalStateException("Titles can not be EMPTY !");
    }

    if (titles.size() != vp.getAdapter().getCount()) {
      throw new IllegalStateException("Titles length must be the same as the page count !");
    }

    this.mViewPager = vp;
    mTitles = new ArrayList<>();
    mTitles.addAll(titles);
    this.mViewPager.removeOnPageChangeListener(this);
    this.mViewPager.addOnPageChangeListener(this);
    notifyDataSetChanged();
  }

  /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
  public void setViewPager(ViewPager vp, String[] titles) {
    ArrayList<String> titleList = new ArrayList<>();
    Collections.addAll(titleList, titles);
    setViewPager(vp, titleList);
  }
  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager vp, List<String> titles, FragmentManager fragmentManager,
      List<Fragment> fragments) {
    mTitles = new ArrayList<>();
    mTitles.addAll(titles);
    if (vp == null) {
      throw new IllegalStateException("ViewPager can not be NULL !");
    }

    if (titles == null || titles.size() == 0) {
      throw new IllegalStateException("Titles can not be EMPTY !");
    }

    this.mViewPager = vp;
    this.mViewPager.setAdapter(
        new InnerPagerAdapter(fragmentManager, fragments, titles));

    this.mViewPager.removeOnPageChangeListener(this);
    this.mViewPager.addOnPageChangeListener(this);
    notifyDataSetChanged();
  }
  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager vp, List<String> titles, FragmentActivity fa,
      List<Fragment> fragments) {
    setViewPager(vp, titles, fa.getSupportFragmentManager(), fragments);
  }

  /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
  public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa,
      List<Fragment> fragments) {

    ArrayList<String> stringList = new ArrayList<>();
    Collections.addAll(stringList, titles);
    setViewPager(vp, stringList, fa,
        fragments);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
  }

  @Override
  public void onPageSelected(int position) {
    updateTabSelection(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
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
    PagerAdapter adapter = mViewPager.getAdapter();
    if (adapter != null) {
      return adapter.getCount();
    }
    return 0;
  }

  @Override int getCurrentItem() {
    return mViewPager.getCurrentItem();
  }

  @Override void setCurrentItem(int position) {
    this.mCurrentTab = position;
    mViewPager.setCurrentItem(position);
  }

  @Override void setCurrentItem(int position, boolean smooth) {
    this.mCurrentTab = position;
    mViewPager.setCurrentItem(position, smooth);
  }
  class InnerPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles;

    public InnerPagerAdapter(FragmentManager fm, List<Fragment> fragments,
        List<String> titles) {
      super(fm);

      this.fragments = fragments;
      this.titles = titles;
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
      // super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
      return PagerAdapter.POSITION_NONE;
    }
  }

}
