package com.xxf.arch.widget.progresshud;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface ProgressHUDProvider<T extends ProgressHUD> {

    T progressHUD();
}
