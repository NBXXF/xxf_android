package me.everything.overscrolldemo.view;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.xxf.effect.overscroll.myadapter.NestedScrollViewOverScrollDecorAdapter;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.overscrolldemo.R;

public class NestedScrollViewDemoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final NestedScrollView rootView = (NestedScrollView) inflater.inflate(R.layout.nested_scrollview_demo, null, false);
        new VerticalOverScrollBounceEffectDecorator(new NestedScrollViewOverScrollDecorAdapter(rootView));
        return rootView;
    }
}
