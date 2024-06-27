package androidx.recyclerview.widget

import android.text.TextUtils

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/26/21
 * Description : 链接内部非公开方法
 */
abstract class InnerListAdapter<T, VH : RecyclerView.ViewHolder> : ListAdapter<T, VH> {
    private val mObserversList: ArrayList<RecyclerView.AdapterDataObserver> =
        ArrayList<RecyclerView.AdapterDataObserver>()

    /**
     * 提供方法 可以获取 这样可以传递给嵌套的adapter
     */
    fun getAdapterDataObservers(): List<RecyclerView.AdapterDataObserver> {
        return mObserversList.filter { it ->
            /**
             * 忽略内部的adapters 内部的adapter 会进行布局等操作
             */
            !TextUtils.equals( it.javaClass.`package`.name,RecyclerView::class.java.`package`.name)
        }
    }

    /**
     * 绑定 避免报错
     */
    fun bindAdapterDataObservers(observers: List<RecyclerView.AdapterDataObserver>) {
        observers.forEach {
            unregisterAdapterDataObserver(it)
            registerAdapterDataObserver(it)
        }
    }

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback)

    constructor(config: AsyncDifferConfig<T>) : super(config)

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
        synchronized(mObserversList) {
            mObserversList.add(observer)
        }
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        synchronized(mObserversList) {
            mObserversList.remove(observer)
        }
    }


}