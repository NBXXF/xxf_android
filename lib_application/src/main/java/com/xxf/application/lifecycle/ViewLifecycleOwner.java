/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxf.application.lifecycle;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.application.R;

/**
 * 解决 Android 本身的fragment 包装了 LifecycleOwner
 * 导致获取的LifecycleOwner 不能转换成fragment
 */
public class ViewLifecycleOwner {
    private ViewLifecycleOwner() {
    }


    public static void set(@NonNull View view, @Nullable LifecycleOwner lifecycleOwner) {
        view.setTag(R.id.tag_view_lifecycle_owner, lifecycleOwner);
    }


    @Nullable
    public static LifecycleOwner get(@NonNull View view) {
        LifecycleOwner found = (LifecycleOwner) view.getTag(R.id.tag_view_lifecycle_owner);
        if (found != null) return found;
        ViewParent parent = view.getParent();
        while (found == null && parent instanceof View) {
            final View parentView = (View) parent;
            found = (LifecycleOwner) parentView.getTag(R.id.tag_view_lifecycle_owner);
            parent = parentView.getParent();
        }
        //解决没有添加到window上的时候
        if (found == null && (view.getContext() instanceof LifecycleOwner)) {
            return (LifecycleOwner) view.getContext();
        }
        return found;
    }
}
