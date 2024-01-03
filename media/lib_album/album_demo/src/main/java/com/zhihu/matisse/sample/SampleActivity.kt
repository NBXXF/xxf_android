package com.zhihu.matisse.sample

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxf.media.album.AlbumLauncher
import com.xxf.media.album.AlbumResult
import com.xxf.media.album.MimeType
import com.xxf.media.album.engine.impl.GlideEngine
import com.xxf.media.album.engine.impl.PicassoEngine
import com.xxf.media.album.filter.Filter
import com.xxf.media.album.internal.entity.CaptureStrategy
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class SampleActivity : AppCompatActivity(), View.OnClickListener {
    private var mAdapter: UriAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler{
            Log.d("=======>", "tag:$it")
        }
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.zhihu).setOnClickListener(this)
        findViewById<View>(R.id.dracula).setOnClickListener(this)
        findViewById<View>(R.id.only_gif).setOnClickListener(this)
        findViewById<View>(R.id.btn_permission).setOnClickListener(this)
        val recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UriAdapter().also { mAdapter = it }
    }

    // <editor-fold defaultstate="collapsed" desc="onClick">
    @SuppressLint("CheckResult")
    override fun onClick(v: View) {
        startAction(v)
    }

    // </editor-fold>
    private fun startAction(v: View) {
        when (v.id) {
            R.id.zhihu -> AlbumLauncher.from(this@SampleActivity)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                    CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test")
                )
                .maxSelectable(9)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                    resources.getDimensionPixelSize(R.dimen.grid_expected_size)
                )
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .setOnSelectedListener { uriList: List<Uri?>?, pathList: List<String?> ->
                    Log.e(
                        "onSelected",
                        "onSelected: pathList=$pathList"
                    )
                }
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener { isChecked: Boolean ->
                    Log.e(
                        "isChecked",
                        "onCheck: isChecked=$isChecked"
                    )
                }
                .forResult()
                .subscribe {albumResult->
                    mAdapter!!.setData(albumResult.uris, albumResult.paths)
                }

            R.id.dracula -> AlbumLauncher.from(this@SampleActivity)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(9)
                .originalEnable(true)
                .maxOriginalSize(10)
                .imageEngine(PicassoEngine())
                .forResult()
                .subscribe{albumResult->
                    mAdapter!!.setData(albumResult.uris, albumResult.paths)
                }

            R.id.only_gif -> AlbumLauncher.from(this@SampleActivity)
                .choose(MimeType.of(MimeType.GIF), false)
                .countable(true)
                .maxSelectable(9)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                    resources.getDimensionPixelSize(R.dimen.grid_expected_size)
                )
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult()
                .subscribe {albumResult->
                    mAdapter!!.setData(albumResult.uris, albumResult.paths)
                }

            R.id.btn_permission -> {

            }
            else -> {}
        }
        mAdapter!!.setData(null, null)
    }

    private class UriAdapter : RecyclerView.Adapter<UriAdapter.UriViewHolder>() {
        private var mUris: List<Uri>? = null
        private var mPaths: List<String>? = null
        fun setData(uris: List<Uri>?, paths: List<String>?) {
            mUris = uris
            mPaths = paths
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UriViewHolder {
            return UriViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.uri_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: UriViewHolder, position: Int) {
            holder.mUri.text = mUris!![position].toString()
            holder.mPath.text = mPaths!![position]
            holder.mUri.alpha = if (position % 2 == 0) 1.0f else 0.54f
            holder.mPath.alpha = if (position % 2 == 0) 1.0f else 0.54f
        }

        override fun getItemCount(): Int {
            return if (mUris == null) 0 else mUris!!.size
        }

        internal class UriViewHolder(contentView: View) : RecyclerView.ViewHolder(contentView) {
            val mUri: TextView
            val mPath: TextView

            init {
                mUri = contentView.findViewById<View>(R.id.uri) as TextView
                mPath = contentView.findViewById<View>(R.id.path) as TextView
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //        AlbumService.INSTANCE.getAlbum(this)
//                .subscribe(new Consumer<List<Item>>() {
//                    @Override
//                    public void accept(List<Item> items) throws Throwable {
//                        System.out.println("=========>all:" + items);
//                    }
//                });
//        AlbumService.INSTANCE.getImages(this)
//                .subscribe(new Consumer<List<Item>>() {
//                    @Override
//                    public void accept(List<Item> items) throws Throwable {
//                        System.out.println("=========>imgs:" + items);
//                    }
//                });
//
//        AlbumService.INSTANCE.getVideos(this)
//                .subscribe(new Consumer<List<Item>>() {
//                    @Override
//                    public void accept(List<Item> strings) throws Throwable {
//                        System.out.println("=========>videos:" + strings);
//                    }
//                });
    }
}