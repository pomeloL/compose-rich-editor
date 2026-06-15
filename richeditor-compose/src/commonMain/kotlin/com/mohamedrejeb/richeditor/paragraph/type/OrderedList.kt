package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.DefaultListIndent
import com.mohamedrejeb.richeditor.model.DefaultOrderedListStyleType
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class OrderedList private constructor(
    number: Int,
    initialIndent: Int = DefaultListIndent,
    startTextWidth: TextUnit = 0.sp,
    initialLevel: Int = 1,
    initialStyleType: OrderedListStyleType = DefaultOrderedListStyleType,
    initialPrefixAlignment: ListPrefixAlignment = ListPrefixAlignment.End,
) : ParagraphType, ConfigurableStartTextWidth, ConfigurableListLevel {

    constructor(
        number: Int,
        initialLevel: Int = 1,
    ) : this(
        number = number,
        initialIndent = DefaultListIndent,
        initialLevel = initialLevel,
    )

    constructor(
        number: Int,
        config: RichTextConfig,
        startTextWidth: TextUnit = 0.sp,
        initialLevel: Int = 1,
    ) : this(
        number = number,
        initialIndent = config.orderedListIndent,
        startTextWidth = startTextWidth,
        initialLevel = initialLevel,
        initialStyleType = config.orderedListStyleType,
        initialPrefixAlignment = config.listPrefixAlignment,
    )

    var number = number
        set(value) {
            field = value
            startRichSpan = getNewStartRichSpan(startRichSpan.textRange)
        }

    override var startTextWidth: TextUnit = startTextWidth
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    private var indent = initialIndent
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    override var level = initialLevel
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    private var styleType = initialStyleType
        set(value) {
            field = value
            startRichSpan = getNewStartRichSpan(startRichSpan.textRange)
        }

    private var prefixAlignment = initialPrefixAlignment
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    private var style: ParagraphStyle =
        getNewParagraphStyle()

    override fun getStyle(config: RichTextConfig): ParagraphStyle {
        if (config.orderedListIndent != indent) {
            indent = config.orderedListIndent
        }

        if (config.orderedListStyleType != styleType) {
            styleType = config.orderedListStyleType
        }

        if (config.listPrefixAlignment != prefixAlignment) {
            prefixAlignment = config.listPrefixAlignment
        }

        return style
    }

    private fun getNewParagraphStyle(): ParagraphStyle {
        val base = (indent * level).toFloat()
        val prefix = startTextWidth.value
        val useEnd = prefixAlignment == ListPrefixAlignment.End && base >= prefix
        return ParagraphStyle(
            textIndent = TextIndent(
                firstLine = if (useEnd) (base - prefix).sp else base.sp,
                restLine = if (useEnd) base.sp else (base + prefix).sp
            )
        )
    }

    override var startRichSpan: RichSpan =
        getNewStartRichSpan()

    @OptIn(ExperimentalRichTextApi::class)
    private fun getNewStartRichSpan(textRange: TextRange = TextRange(0)): RichSpan {
        val text = styleType.format(number, level) + styleType.getSuffix(level)

        return RichSpan(
            paragraph = RichParagraph(type = this),
            text = text,
            textRange = TextRange(
                textRange.min,
                textRange.min + text.length
            )
        )
    }

    override fun getNextParagraphType(): ParagraphType =
        OrderedList(
            number = number + 1,
            initialIndent = indent,
            startTextWidth = startTextWidth,
            initialLevel = level,
            initialStyleType = styleType,
            initialPrefixAlignment = prefixAlignment,
        )

    override fun copy(): ParagraphType =
        OrderedList(
            number = number,
            initialIndent = indent,
            startTextWidth = startTextWidth,
            initialLevel = level,
            initialStyleType = styleType,
            initialPrefixAlignment = prefixAlignment,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderedList) return false

        if (number != other.number) return false
        if (indent != other.indent) return false
        if (startTextWidth != other.startTextWidth) return false
        if (level != other.level) return false
        if (styleType != other.styleType) return false
        if (prefixAlignment != other.prefixAlignment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = indent
        result = 31 * result + number
        result = 31 * result + indent
        result = 31 * result + startTextWidth.hashCode()
        result = 31 * result + level
        result = 31 * result + styleType.hashCode()
        result = 31 * result + prefixAlignment.hashCode()
        return result
    }
}
