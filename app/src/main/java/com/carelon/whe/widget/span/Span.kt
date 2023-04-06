package com.carelon.whe.widget.span

import android.graphics.Typeface
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.carelon.whe.widget.custom_typeface.CustomTypeface

class Span(val parent: Span? = null) : SpannableStringBuilder() {

    companion object {
        val EMPTY_STYLE = Span()

        var globalStyle: Span = EMPTY_STYLE
    }

    var text: CharSequence = ""

    @ColorInt
    var textColor: Int? = parent?.textColor

    @ColorInt var backgroundColor: Int? = parent?.backgroundColor

    @Dimension(unit = Dimension.PX) var textSize: Int? = parent?.textSize

    var fontFamily: String? = parent?.fontFamily

    var typeface: Typeface? = parent?.typeface

    var textStyle: String? = parent?.textStyle

    var alignment: String? = parent?.alignment

    var textDecorationLine: String? = parent?.textDecorationLine

    @Dimension(unit = Dimension.PX) var lineSpacing: Int? = null

    @Dimension(unit = Dimension.PX) var paddingTop: Int? = null

    @Dimension(unit = Dimension.PX) var paddingBottom: Int? = null

    @Dimension(unit = Dimension.PX) var verticalPadding: Int? = null

    var onClick: (() -> Unit)? = null

    var spans: ArrayList<Any> = ArrayList()

    var style: Span = EMPTY_STYLE

    private fun buildCharacterStyle(builder: ArrayList<Any>) {
        if (textColor != null) {
            builder.add(ForegroundColorSpan(textColor!!))
        }

        if (backgroundColor != null) {
            builder.add(BackgroundColorSpan(backgroundColor!!))
        }

        if (textSize != null) {
            builder.add(AbsoluteSizeSpan(textSize!!))
        }

        if (!TextUtils.isEmpty(fontFamily)) {
            builder.add(TypefaceSpan(fontFamily))
        }

        if (typeface != null) {
            builder.add(CustomTypeface(typeface!!))
        }

        if (!TextUtils.isEmpty(textStyle)) {
            builder.add(StyleSpan(when (textStyle) {
                "normal" -> Typeface.NORMAL
                "bold" -> Typeface.BOLD
                "italic" -> Typeface.ITALIC
                "bold_italic" -> Typeface.BOLD_ITALIC
                else -> throw RuntimeException("Unknown text style")
            }))
        }
    }

    private fun buildParagraphStyle(builder: ArrayList<Any>) {
        if (!TextUtils.isEmpty(alignment)) {
            builder.add(AlignmentSpan.Standard(when (alignment) {
                "normal" -> Layout.Alignment.ALIGN_NORMAL
                "opposite" -> Layout.Alignment.ALIGN_OPPOSITE
                "center" -> Layout.Alignment.ALIGN_CENTER
                else -> throw RuntimeException("Unknown text alignment")
            }))
        }

        paddingTop = when {
            paddingTop != null -> paddingTop
            verticalPadding != null -> verticalPadding
            else -> 0
        }
        paddingBottom = when {
            paddingBottom != null -> paddingBottom
            verticalPadding != null -> verticalPadding
            else -> 0
        }
    }

    private fun preBuild() {
        override(style)
    }

    fun build(): Span {
        preBuild()
        val builder = ArrayList<Any>()
        if (!TextUtils.isEmpty(text)) {
            var p = this.parent
            while (p != null) {
                if (!TextUtils.isEmpty(p.text)) {
                    throw RuntimeException("Can't nest \"$text\" in spans")
                }
                p = p.parent
            }
            append(text)
            buildCharacterStyle(builder)
            buildParagraphStyle(builder)
        } else {
            buildParagraphStyle(builder)
        }

        builder.addAll(spans)
        builder.forEach {
            setSpan(it, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return this
    }

    fun override(style: Span) {
        if (textColor == null) {
            textColor = style.textColor
        }
        if (backgroundColor == null) {
            backgroundColor = style.backgroundColor
        }
        if (textSize == null) {
            textSize = style.textSize
        }
        if (fontFamily == null) {
            fontFamily = style.fontFamily
        }
        if (typeface == null) {
            typeface = style.typeface
        }
        if (textStyle == null) {
            textStyle = style.textStyle
        }
        if (alignment == null) {
            alignment = style.alignment
        }
        if (textDecorationLine == null) {
            textDecorationLine = style.textDecorationLine
        }
        if (lineSpacing == null) {
            lineSpacing = style.lineSpacing
        }
        if (paddingTop == null) {
            paddingTop = style.paddingTop
        }
        if (paddingBottom == null) {
            paddingBottom = style.paddingBottom
        }
        if (verticalPadding == null) {
            verticalPadding = style.verticalPadding
        }
        if (onClick == null) {
            onClick = style.onClick
        }
        spans.addAll(style.spans)
    }

    operator fun CharSequence.unaryPlus(): CharSequence {
        return append(Span(parent = this@Span).apply {
            text = this@unaryPlus
            build()
        })
    }

    operator fun Span.plus(other: CharSequence): CharSequence {
        return append(Span(parent = this).apply {
            text = other
            build()
        })
    }
}

fun span(init: Span.() -> Unit): Span = Span().apply {
    override(Span.globalStyle)
    init()
    build()
}


fun style(init: Span.() -> Unit): Span = Span().apply {
    init()
}

fun Span.span(init: Span.() -> Unit = {}): Span = apply {
    append(Span(parent = this).apply {
        init()
        build()
    })
}
