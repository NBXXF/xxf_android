package com.xxf.view.actiondialog.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import com.xxf.view.actiondialog.ItemMenuImpl;
import com.xxf.view.actiondialog.SystemUtils;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class CameraMenuItem extends ItemMenuImpl<String> {

    private FragmentActivity activity;
    Consumer<String> consumer;

    public CameraMenuItem(FragmentActivity activity, Consumer<String> consumer) {
        super("拍照", "拍照");
        this.activity = activity;
        this.consumer = consumer;
    }

    @Override
    public void doAction() {
        super.doAction();
        SystemUtils.doTakePhoto(activity, this.consumer);
    }

}
