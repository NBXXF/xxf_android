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
import com.liulishuo.okdownload.core.Util
import com.nbxxf.kpower.database.model.BasePageInfoDTO
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus
import com.xxf.download.listener.DownloadUpdateListener
import com.xxf.download.model.IDownloadEntity
import com.xxf.download.model.taskModel
import com.xxf.ktx.isMainThread
import com.xxf.speed.collections.toArrayListOrCast
import java.io.File
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/8/19 12:07 PM
 * Description: 下载任务队列抽象
 */
abstract class DownloadService<T : IDownloadEntity> : Service(), IDownloadService<T> {

    companion object {
        private val SERIAL_EXECUTOR: Executor = ThreadPoolExecutor(
            0,
            Int.MAX_VALUE, 30, TimeUnit.SECONDS, SynchronousQueue(),
            Util.threadFactory("DownloadService DynamicSerial", false)
        )
        const val ACTION_ADD_TASKS = "xxf.download.action.addTasks"
        const val KEY_TASKS = "tasks"

        private fun <T : IDownloadEntity, O : IDownloadService<T>> buildTaskIntent(
            context: Context,
            target: Class<O>,
            tasks: List<T>
        ): Intent {
            return Intent(context, target)
                .apply {
                    action = ACTION_ADD_TASKS
                    putExtra(KEY_TASKS, tasks.toArrayListOrCast())
                }
        }

        /**
         * 开启service 并启动任务
         */
        fun <T : IDownloadEntity, O : IDownloadService<T>> Class<O>.startService(
            context: Context,
            tasks: List<T> = arrayListOf()
        ) {
            context.startService(buildTaskIntent(context, this, tasks))
        }

        /**
         * 停止service
         */
        fun <T : IDownloadEntity, O : IDownloadService<T>> Class<O>.stopService(context: Context) {
            context.stopService(buildTaskIntent(context, this, arrayListOf()))
        }


        /**
         * bindService 并启动任务
         */
        fun <T : IDownloadEntity, O : IDownloadService<T>> Class<O>.bindService(
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
    private val mListenerWrapper =
        DownloaderListenerWrapper(mutableListOf(object : DownloadUpdateListener<T>() {
            override fun updateDownload(task: T?, info: DownloadInfo) {
                this@DownloadService.updateDownload(task, info)
            }
        }))
    private var mSerialQueue: InnerDownloadSerialQueue =
        InnerDownloadSerialQueue(mListenerWrapper)
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
        SERIAL_EXECUTOR.executeIfChildThread {
            getCacheService().insert(tasks
                .filter {
                    //避免加入非http的地址的数据 导致队列一直闪退
                    //DownloadOkHttp3Connection.java:48
                    it.downloadUrl.startsWith("http")
                }
                .map {
                    it.createDate = Date()
                    it
                })
            resumeTask(tasks)
        }

    }


    override fun resumeTasks() {
        SERIAL_EXECUTOR.executeIfChildThread {
            mSerialQueue.shutdown()
            mSerialQueue = InnerDownloadSerialQueue(mListenerWrapper)
            val unfinished = getCacheService().selectPage(1, 300) {
                it.notEqual(IDownloadEntity::downloadStatus, DownloadStatus.COMPLETED.value)
                //只默认恢复5次之内失败的 避免大量任务堵塞
                it.lessOrEqual(IDownloadEntity::downloadErrorTimes, 5L)
                it.order(IDownloadEntity::createDate, true)
                it
            }.list
            resumeTask(unfinished)
        }
    }

    override fun resumeTask(tasks: List<T>) {
        SERIAL_EXECUTOR.executeIfChildThread {
            tasks.forEach {
                if (!mSerialQueue.contains(it)) {
                    mSerialQueue.enqueue(onConvertTask(it))
                }
            }
            mSerialQueue.resume()
        }
    }


    override fun pauseTasks() {
        SERIAL_EXECUTOR.executeIfChildThread {
            mSerialQueue.pause()
        }
    }

    override fun pauseTask(tasks: List<T>) {
        SERIAL_EXECUTOR.executeIfChildThread {
            mSerialQueue.remove(tasks)
        }
    }

    override fun removeTask(tasks: List<T>) {
        SERIAL_EXECUTOR.executeIfChildThread {
            mSerialQueue.cancel(tasks.map { task ->
                onConvertTask(task)
            })
            getCacheService().deleteById(tasks.map { it.id() })
            tasks.forEach {
                File(it.downloadPath).deleteRecursively()
            }
        }
    }

    override fun getTasks(pageNum: Long, pageSize: Long, desc: Boolean): BasePageInfoDTO<T> {
        return getCacheService().selectPage(pageNum, pageSize) {
            it.order(IDownloadEntity::createDate, desc)
            it
        }
    }

    override fun getTasks(
        pageNum: Long,
        pageSize: Long,
        desc: Boolean,
        status: Long
    ): BasePageInfoDTO<T> {
        return getCacheService().selectPage(pageNum, pageSize) {
            it.equal(IDownloadEntity::downloadStatus, status)
            it.order(IDownloadEntity::createDate, desc)
            it
        }
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

    /**
     * 转换任务到内部的task
     */
    @JvmOverloads
    protected open fun onConvertTask(task: T): DownloadTask {
        return DownloadTask.Builder(
            task.downloadUrl,
            File(task.downloadPath)
        ).setConnectionCount(1)
            .setHeaderMapFields(mHeaderMapFields)
            .setWifiRequired(mWifiRequired)
            /**
             * 有持久化api 不要回调到主线程
             */
            .setAutoCallbackToUIThread(false)
            .setReadBufferSize(DownloadTask.Builder.DEFAULT_READ_BUFFER_SIZE * 2)
            .setFlushBufferSize(DownloadTask.Builder.DEFAULT_FLUSH_BUFFER_SIZE * 2)
            .build()
            .apply {
                this.taskModel = task
            }
    }

    /**
     * 更新下载状态
     */
    protected open fun updateDownload(task: T?, info: DownloadInfo) {
        when (info.status) {
            DownloadStatus.CONNECT -> {
                if ((info.totalLength ?: 0) > 0L) {
                    val taskModel = requireNotNull(task)
                    val selectById = getCacheService().selectById(taskModel.id())
                        ?: taskModel
                    selectById.downloadTotalLength = info.totalLength!!
                    getCacheService().insertOrUpdate(listOf(selectById))
                }
            }

            DownloadStatus.COMPLETED -> {
                val taskModel = requireNotNull(task)
                val selectById = getCacheService().selectById(taskModel.id())
                    ?: taskModel
                selectById.downloadStatus = info.status.value
                getCacheService().insertOrUpdate(listOf(selectById))
            }

            DownloadStatus.ERROR -> {
                val taskModel = requireNotNull(task)
                taskModel.downloadErrorTimes = 0L
                val selectById = getCacheService().selectById(taskModel.id())
                    ?: taskModel
                selectById.downloadStatus = info.status.value
                selectById.downloadErrorTimes += 1
                getCacheService().insertOrUpdate(listOf(selectById))
            }

            else -> {

            }
        }
    }

    /**
     * 处理线程问题 如果已经是子线程了 就在对应的线程执行
     */
    private fun Executor.executeIfChildThread(command: Runnable) {
        if (isMainThread) {
            this.execute(command)
        } else {
            command.run()
        }
    }
}