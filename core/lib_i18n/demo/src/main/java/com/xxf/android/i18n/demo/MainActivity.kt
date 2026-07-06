package com.xxf.android.i18n.demo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.xxf.android.i18n.appLanguage
import com.xxf.android.i18n.getLocalizedQuantityString
import com.xxf.android.i18n.getLocalizedString
import com.xxf.android.i18n.getLocalizedStringArray
import com.xxf.android.i18n.resetAppLanguage
import com.xxf.android.i18n.resString
import com.xxf.android.i18n.setAppLanguage

class MainActivity : AppCompatActivity() {

    private lateinit var contentView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentView = findViewById(R.id.content_view)

        findViewById<Button>(R.id.btn_system).setOnClickListener {
            resetAppLanguage()
            render()
        }
        findViewById<Button>(R.id.btn_zh).setOnClickListener {
            setAppLanguage("zh-CN")
            render()
        }
        findViewById<Button>(R.id.btn_en).setOnClickListener {
            setAppLanguage("en")
            render()
        }
        findViewById<Button>(R.id.btn_refresh).setOnClickListener {
            render()
        }

        render()
    }

    private fun render() {
        val languageTags = appLanguage.toLanguageTag()
        val title = getLocalizedString(R.string.demo_title)
        val titleByExt = R.string.demo_title.resString()
        val arrayText = getLocalizedStringArray(R.array.demo_languages).joinToString(" | ")
        val quantityText = getLocalizedQuantityString(R.plurals.demo_count, 2, 2)

        contentView.text = buildString {
            appendLine("appLanguage: $languageTags")
            appendLine("getLocalizedString: $title")
            appendLine("resString: $titleByExt")
            appendLine("array: $arrayText")
            appendLine("quantity: $quantityText")
        }
    }
}
