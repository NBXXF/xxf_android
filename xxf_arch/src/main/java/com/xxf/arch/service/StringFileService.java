package com.xxf.arch.service;

import android.Manifest;

import androidx.annotation.RequiresPermission;

import java.io.File;

import io.reactivex.Observable;

/**
 * @Description: 文件服务
 * @Author: XGod
 * @CreateDate: 2020/6/14 12:01
 */
public interface StringFileService extends UserFileService {

    static StringFileService getDefault() {
        return XXFileServiceImpl.getInstance();
    }

    /**
     * 获取文件
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @return
     */
    Observable<String> getUserPrivateFile(String childFileName);

    /**
     * 获取文件
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @return
     */
    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<String> getUserPublicFile(String childFileName);

    /**
     * 获取文件
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @return
     */
    Observable<String> getPrivateFile(String childFileName);

    /**
     * 获取文件
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @return
     */
    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<String> getPublicFile(String childFileName);

    /**
     * 添加到用户的私有文件区域,区分用户
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @param content
     * @param append        是否追加模式,否则覆盖
     * @return
     */
    Observable<File> putUserPrivateFile(String childFileName, String content, boolean append);

    /**
     * 添加到用户的公共区域,区分用户
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @param content
     * @param append        是否追加模式,否则覆盖
     * @return
     */
    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<File> putUserPublicFile(String childFileName, String content, boolean append);


    /**
     * 添加私有文件区域
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @param content
     * @param append        是否追加模式,否则覆盖
     * @return
     */
    Observable<File> putPrivateFile(String childFileName, String content, boolean append);

    /**
     * 添加到公共区域
     *
     * @param childFileName ！！！注意是子文件eg. xx.doc； "/doc/xxx.doc" 不要包含根路径
     * @param content
     * @param append        是否追加模式,否则覆盖
     * @return
     */
    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Observable<File> putPublicFile(String childFileName, String content, boolean append);


}
