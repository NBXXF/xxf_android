package com.xxf.view.actiondialog.menu;

import androidx.fragment.app.FragmentActivity;


import com.xxf.view.actiondialog.ItemMenuImpl;
import com.xxf.view.actiondialog.SystemUtils;

import io.reactivex.functions.Consumer;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class AlbumMenuItem extends ItemMenuImpl<String> {

    private FragmentActivity activity;
    Consumer<String> consumer;


    public AlbumMenuItem(FragmentActivity activity, Consumer<String> consumer) {
        super("从手机相册选取", "从手机相册选取");
        this.activity = activity;
        this.consumer = consumer;
    }

    @Override
    public void doAction() {
        super.doAction();
        SystemUtils.doSelectAlbum(activity, this.consumer);
    }
}
