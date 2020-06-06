package com.xxf.arch.arouter;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 模块暴露服务, 给其他Module使用
 * <p>
 * 使用方法:继承此类 并添加注解 @Route(path = "/yourservicegroupname/hello", name = "测试服务")
 */
public abstract class XXFModuleProvider implements IProvider {
}
