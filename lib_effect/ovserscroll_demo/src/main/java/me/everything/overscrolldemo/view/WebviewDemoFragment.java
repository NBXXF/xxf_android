package me.everything.overscrolldemo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.xxf.effect.overscroll.myadapter.NestedScrollViewOverScrollDecorAdapter;
import com.xxf.effect.overscroll.myadapter.WebViewOverScrollDecorAdapter;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.overscrolldemo.R;

public class WebviewDemoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final WebView rootView = (WebView) inflater.inflate(R.layout.webview_scrollview_demo, null, false);
        new VerticalOverScrollBounceEffectDecorator(new WebViewOverScrollDecorAdapter(rootView));
        rootView.getSettings().setDatabaseEnabled(true);
        rootView.getSettings().setDomStorageEnabled(true);
        rootView.getSettings().setAllowFileAccess(true);
        rootView.loadUrl("https://www.jianshu.com/p/055fa3cb964e");
        return rootView;
    }
}
