package com.xxf.arch.widget.progresshud;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public interface ProgressHUDProvider<T extends ProgressHUD> {

    T progressHUD();
}
