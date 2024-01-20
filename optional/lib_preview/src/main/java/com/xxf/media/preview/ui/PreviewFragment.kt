package com.xxf.media.preview.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.xxf.media.preview.Config
import com.xxf.media.preview.databinding.XxfFragmentPreviewBinding
import com.xxf.media.preview.model.url.ImageThumbAutoOriginUrl
import com.xxf.media.preview.model.url.ImageUrl
import com.xxf.media.preview.model.url.VideoImageUrl

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description ://
 */
open class PreviewFragment : Fragment() {
    protected lateinit var url: ImageUrl;
    protected lateinit var binding: XxfFragmentPreviewBinding;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            if (this.binding.root.parent != null) {
                (this.binding.root.parent as ViewGroup).removeView(this.binding.root)
            }
        } else {
            binding = XxfFragmentPreviewBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments!!.getSerializable(Config.PREVIEW_PARAM) as ImageUrl
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnViewTapListener { view, x, y ->
            requireActivity().onBackPressed()
        }
        binding.imageView.setOnPhotoTapListener(object : OnPhotoTapListener {
            override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
                requireActivity().onBackPressed()
            }
        })
        binding.imageView.setOnOutsidePhotoTapListener {
            requireActivity().onBackPressed()
        }
        loadImage()
    }

    open fun loadImage() {
        if (url is ImageThumbAutoOriginUrl) {
            binding.imageView.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            var request = Glide.with(this)
                .load((url as ImageThumbAutoOriginUrl).originUrl)
                .priority(Priority.IMMEDIATE)
                .thumbnail(
                    Glide.with(this)
                        .load(url.url)
                        .priority(Priority.IMMEDIATE)
                        //避免缩略图太慢导致动画不能执行
                        .timeout(300)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                requireActivity().supportStartPostponedEnterTransition()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                requireActivity().supportStartPostponedEnterTransition()
                                return false
                            }
                        })
                );
            if (url.placeholderResourceId > 0) {
                request = request.placeholder(url.placeholderResourceId)
            }
            if (url.errorResourceId > 0) {
                request = request.error(url.placeholderResourceId)
            }
            request
                .into(object : DrawableImageViewTarget(binding.imageView) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        requireActivity().supportStartPostponedEnterTransition()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        requireActivity().supportStartPostponedEnterTransition()
                    }
                })
        } else if (url is VideoImageUrl) {
            binding.imageView.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.videoView.setOnPreparedListener {
                binding.progressBar.visibility = View.GONE
            }
            binding.videoView.setOnErrorListener { mp, what, extra ->
                binding.progressBar.visibility = View.GONE
                Log.d("=======>error", "what:$what  extra:$extra");
                false
            }
            binding.videoView.setVideoPath((url as VideoImageUrl).sourceUrl)
            binding.videoView.start()
            requireActivity().supportStartPostponedEnterTransition()
        } else {
            binding.imageView.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            var request = Glide.with(this)
                .load(url.url)
                .priority(Priority.IMMEDIATE)
                .thumbnail(
                    Glide.with(this)
                        .load(url.url)
                        .priority(Priority.IMMEDIATE)
                        .thumbnail(0.8f)
                        //避免缩略图太慢导致动画不能执行
                        .timeout(300)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                requireActivity().supportStartPostponedEnterTransition()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                requireActivity().supportStartPostponedEnterTransition()
                                return false
                            }
                        })
                )
            if (url.placeholderResourceId > 0) {
                request = request.placeholder(url.placeholderResourceId)
            }
            if (url.errorResourceId > 0) {
                request = request.error(url.placeholderResourceId)
            }
            request
                .into(object : DrawableImageViewTarget(binding.imageView) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        requireActivity().supportStartPostponedEnterTransition()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        requireActivity().supportStartPostponedEnterTransition()
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        if (url is ImageThumbAutoOriginUrl) {
            binding.videoView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (url is ImageThumbAutoOriginUrl) {
            binding.videoView.pause()
        }
    }
}