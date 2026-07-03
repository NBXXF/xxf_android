package com.xxf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.InnerViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.view.recyclerview.adapter.IEdgeEffectViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/16/21 2:49 PM
 * Description: 适合 ConcatAdapter 做header footer
 * <p>
 * 实例如下:
 * ConcatAdapter totalAdapter = new ConcatAdapter();
 * totalAdapter.addAdapter(new XXFViewAdapter(headerView))
 * totalAdapter.addAdapter(new XXFViewAdapter(headerView))
 * totalAdapter.addAdapter(new XXFViewAdapter(footerView))
 */
public class XXFViewSpringAnimAdapter extends XXFViewAdapter {


    public XXFViewSpringAnimAdapter(@NonNull View itemView) {
        super(itemView);
    }

    public XXFViewSpringAnimAdapter(@NonNull View itemView, long itemId) {
        super(itemView, itemId);
    }

    public XXFViewSpringAnimAdapter(int id, RecyclerView recyclerView) {
        super(id, recyclerView);
    }

    public XXFViewSpringAnimAdapter(int id, RecyclerView recyclerView, long itemId) {
        super(id, recyclerView, itemId);
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (itemView.getParent() != null && itemView.getParent() instanceof ViewGroup) {
            ((ViewGroup) itemView.getParent()).removeView(itemView);
        }
        return new SpringInnerViewHolder(itemView);
    }


    public static class SpringInnerViewHolder extends InnerViewHolder implements IEdgeEffectViewHolder<SpringAnimation> {

        public SpringInnerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private SpringAnimation translationY = null;
        private SpringAnimation translationX = null;

        @NotNull
        @Override
        public DynamicAnimation<SpringAnimation> getEdgeEffectAnimation() {
            if (getRecyclerView().getLayoutManager() instanceof LinearLayoutManager
                    && ((LinearLayoutManager) getRecyclerView().getLayoutManager()).getOrientation() == RecyclerView.HORIZONTAL) {
                if (translationX == null) {
                    translationX = new SpringAnimation(itemView, SpringAnimation.TRANSLATION_X)
                            .setSpring(
                                    new SpringForce()
                                            .setFinalPosition(0f)
                                            .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                                            .setStiffness(SpringForce.STIFFNESS_LOW)
                            );
                }
                return translationX;
            } else {
                if (translationY == null) {
                    translationY = new SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
                            .setSpring(
                                    new SpringForce()
                                            .setFinalPosition(0f)
                                            .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                                            .setStiffness(SpringForce.STIFFNESS_LOW)
                            );
                }
                return translationY;
            }
        }
    }
}
