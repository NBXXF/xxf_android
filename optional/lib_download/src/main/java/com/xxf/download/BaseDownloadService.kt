package com.xxf.download

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadSerialQueue
import com.liulishuo.okdownload.DownloadTask
import java.io.File


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/8/19 12:07 PM
 * Description: 下载任务队列抽象
 */
abstract class BaseDownloadService<T : DownloadModel> : Service(), IDownloadService<T> {

    companion object {
        private const val KEY_TASK = "task";
        fun launch(context: Context) {
            context.startService(
                Intent(context, BaseDownloadService::class.java)
            )
        }

        fun bind(context: Context, connection: ServiceConnection): Boolean {
            return context.bindService(
                Intent(context, BaseDownloadService::class.java), connection, BIND_AUTO_CREATE
            )
        }

        fun unBind(context: Context, connection: ServiceConnection) {
            context.unbindService(connection)
        }
    }

    private val mBinder: IBinder = LocalBinder()
    private val mListenerWrapper = DownloaderListenerWrapper()
    private val mSerialQueue: DownloadSerialQueue = DownloadSerialQueue(mListenerWrapper)

    inner class LocalBinder : Binder() {
        fun getService(): BaseDownloadService<*> {
            return this@BaseDownloadService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun addListener(l: DownloadListener) {
        mListenerWrapper.addListener(l)
    }

    override fun removeListener(l: DownloadListener) {
        mListenerWrapper.removeListener(l)
    }

    override fun addTask(tasks: List<T>) {
        if (tasks.isEmpty()) {
            return
        }
        getBox().put(tasks)
        tasks.forEach {
            val url = it.getDownloadUrl();
            mSerialQueue.enqueue(
                DownloadTask.Builder(
                    url, File(it.getDownloadPath())
                ).setConnectionCount(1).build()
            )
        }
    }

    override fun resumeTasks() {

    }

    override fun getTasks(offset: Long, limit: Long): List<T> {
        return getBox()
            .query()
            .build()
            .find(offset, limit)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        (intent?.getSerializableExtra(KEY_TASK) as? T)?.let {
            addTask(listOf(it))
        }
        return super.onStartCommand(intent, flags, startId)
    }

}