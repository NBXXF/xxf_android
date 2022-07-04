package com.xxf.arch.test

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener
import androidx.core.view.ViewCompat
import by.kirich1409.viewbindingdelegate.viewBinding
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
        ViewCompat.setOnReceiveContentListener(
            binding.view,
            MIME_TYPES,
            MyReceiver()
        )
        ViewCompat.setOnReceiveContentListener(
            binding.view2,
            MIME_TYPES,
            MyReceiver()
        )

        binding.view2.setOnDragListener(View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> binding.text.text = "" + (event.clipData.getItemAt(0).uri)
                else -> {}
            }
            true
        })
    }
}