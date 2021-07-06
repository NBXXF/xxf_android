package com.xxf.bus

import java.io.Serializable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/7/21
 * Description :通用分类事件模型 类似androd broadcast对应的广播 有action 有data
 *
 *
 * 推荐使用方式 用ActionTypeEvent.create
 */
class ActionTypeEvent(var action: String, var data: Any) : Serializable, Cloneable {
    override fun toString(): String {
        return "ActionTypeEvent{" +
                "action='" + action + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        //享元 clone 副本更快
        private val TemplateEvent = ActionTypeEvent(ActionTypeEvent::class.java.name, ActionTypeEvent::class.java.name)
        @JvmStatic
        fun create(action: String, data: Any): ActionTypeEvent {
            try {
                val event = TemplateEvent.clone() as ActionTypeEvent
                event.action = action
                event.data = data
                return event
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }
            return ActionTypeEvent(action, data)
        }
    }
}