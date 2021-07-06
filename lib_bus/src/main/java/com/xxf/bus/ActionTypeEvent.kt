package com.xxf.bus;

import android.util.Log;

import java.io.Serializable;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/7/21
 * Description :通用分类事件模型 类似androd broadcast对应的广播 有action 有data
 * <p>
 * 推荐使用方式 用ActionTypeEvent.create
 */
public class ActionTypeEvent implements Serializable, Cloneable {
    //享元 clone 副本更快
    private static ActionTypeEvent TemplateEvent = new ActionTypeEvent(ActionTypeEvent.class.getName(), ActionTypeEvent.class.getName());

    public static ActionTypeEvent create(String action, Object data) {
        try {
            ActionTypeEvent event = (ActionTypeEvent) TemplateEvent.clone();
            event.action = action;
            event.data = data;
            return event;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ActionTypeEvent(action, data);
    }

    private String action;
    private Object data;

    public ActionTypeEvent(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ActionTypeEvent{" +
                "action='" + action + '\'' +
                ", data=" + data +
                '}';
    }
}
