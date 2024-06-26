package com.xxf.arch.test

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener
import androidx.core.view.ViewCompat
import com.xxf.viewbinding.viewBinding
import com.xxf.arch.test.databinding.ActivityReciveBinding

class ReciveContentActivity : AppCompatActivity(R.layout.activity_recive) {
    val MIME_TYPES = arrayOf("image/*", "video/*")

    // (1) Define the listener
    inner class MyReceiver : OnReceiveContentListener {
        override fun onReceiveContent(view: View, payload: ContentInfoCompat): ContentInfoCompat? {
            // Split the incoming content into two groups: content URIs and everything else.
            // This way we can implement custom handling for URIs and delegate the rest.
            val split = payload.partition { item: ClipData.Item -> item.uri != null }
            val uriContent = split.first
            val remaining = split.second
            val uris = StringBuffer()
            if (uriContent != null) {
                val clip = uriContent.clip
                for (i in 0 until clip.itemCount) {
                    val uri = clip.getItemAt(i).uri
                    uris.append(uri.path + "\n")
                }
            }
            binding.text.text = "收到:$uris"
            // Return anything that we didn't handle ourselves. This preserves the default platform
            // behavior for text and anything else for which we are not implementing custom handling.
            return remaining
        }
    }

    private val binding by viewBinding(ActivityReciveBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ViewCompat.setOnReceiveContentListener(
//            binding.view,
//            MIME_TYPES,
//            MyReceiver()
//        )
//        ViewCompat.setOnReceiveContentListener(
//            binding.view2,
//            MIME_TYPES,
//            MyReceiver()
//        )

        binding.view2.setOnDragListener(View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    binding.text.text = "v2:" + (event.clipData.getItemAt(0).uri)
                    println("=========>re:v2");
                }

                else -> {}
            }
            true
        })


        binding.view.setOnDragListener(View.OnDragListener { view, event ->
            println("=========>event:${event.action}");
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    binding.text.text =
                        "rootxx:" + (event.clipData.getItemAt(0).uri)
                    println("=========>re:root");
                }
                else -> {}
            }
            true
        })
    }
}