package com.xxf.download

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.InnerDownloadSerialQueue
import com.liulishuo.okdownload.StatusUtil
import java.io.File


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/8/19 12:07 PM
 * Description: 下载任务队列抽象
 */
abstract class DownloadService<T : IDownloadModel> : Service(), IDownloadService<T> {

    companion object {
        const val ACTION_ADD_TASKS = "xxf.download.action.addTasks"
        const val KEY_TASKS = "tasks"

        private fun <T : IDownloadModel, O : IDownloadService<T>> buildTaskIntent(
            context: Context,
            target: Class<O>,
            tasks: ArrayList<T>
        ): Intent {
            return Intent(context, target)
                .apply {
                    action = ACTION_ADD_TASKS
                    putExtra(KEY_TASKS, tasks)
                }
        }

        /**
         * 开启service 并启动任务
         */
        fun <T : IDownloadModel, O : IDownloadService<T>> Class<O>.startService(
            context: Context,
            tasks: ArrayList<T> = arrayListOf()
        ) {
            context.startService(buildTaskIntent(context, this, tasks))
        }

        /**
         * 停止service
         */
        fun <T : IDownloadModel, O : IDownloadService<T>> Class<O>.stopService(context: Context) {
            context.stopService(buildTaskIntent(context, this, arrayListOf()))
        }


        /**
         * bindService 并启动任务
         */
        fun <T : IDownloadModel, O : IDownloadService<T>> Class<O>.bindService(
            context: Context,
            connection: ServiceConnection,
            tasks: ArrayList<T> = arrayListOf()
        ) {
            context.bindService(buildTaskIntent(context, this, tasks), connection, BIND_AUTO_CREATE)
        }


        /**
         * 取消绑定
         */
        fun <T : ServiceConnection> T.unbindService(context: Context) = context.unbindService(this)
    }

    private val mBinder: IBinder = LocalBinder()
    private val mListenerWrapper = DownloaderListenerWrapper()
    private val mTaskList = arrayListOf<DownloadTask>()
    private var mSerialQueue: InnerDownloadSerialQueue =
        InnerDownloadSerialQueue(mListenerWrapper, mTaskList)
    private var mWifiRequired: Boolean = false
    private var mHeaderMapFields: MutableMap<String, List<String>> = mutableMapOf()

    inner class LocalBinder : Binder() {
        fun getService(): DownloadService<T> {
            return this@DownloadService
        }
    }


    override fun wifiRequired(required: Boolean) {
        mWifiRequired = required
    }

    override fun requestHeaders(headerMapFields: Map<String, List<String>>) {
        mHeaderMapFields.clear()
        mHeaderMapFields.putAll(headerMapFields)
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
        onSaveTasks(tasks)
        tasks.forEach {
            mSerialQueue.enqueue(onConvertTask(it))
            mSerialQueue.resume()
        }
    }

    /**
     * 转换任务到内部的task
     */
    @JvmOverloads
    protected fun onConvertTask(task: T): DownloadTask {
        return DownloadTask.Builder(
            task.getDownloadUrl(),
            File(task.getDownloadPath())
        ).setConnectionCount(1)
            .setHeaderMapFields(mHeaderMapFields)
            .setWifiRequired(mWifiRequired)
            .build()
    }

    override fun resumeTasks() {
        mSerialQueue.shutdown()
        mTaskList.clear()
        mSerialQueue = InnerDownloadSerialQueue(mListenerWrapper, mTaskList)
        getTasks(0, 300, true).list.filter {
            val downloadFile = File(it.getDownloadPath())
            val url = it.getDownloadUrl()
            !StatusUtil.isCompleted(url, downloadFile.parent, downloadFile.name)
        }.forEach {
            mSerialQueue.enqueue(onConvertTask(it))
        }
        mSerialQueue.resume()
    }


    override fun pauseTasks() {
        mSerialQueue.pause()
    }

    override fun removeTask(tasks: List<T>) {
        mSerialQueue.cancel(tasks.map { task ->
            onConvertTask(task)
        })
        onDeleteTask(tasks)
    }

    override fun onBind(intent: Intent?): IBinder {
        intent?.let { handleIntent(it) }
        return mBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handleIntent(it) }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSerialQueue.pause()
    }

    /**
     * 处理意图
     */
    @Suppress("UNCHECKED_CAST")
    private fun handleIntent(intent: Intent) {
        when (intent.action.orEmpty()) {
            ACTION_ADD_TASKS -> {
                (intent.extras?.get(KEY_TASKS) as? List<T>)?.let {
                    addTask(it)
                }
            }
        }
    }

}