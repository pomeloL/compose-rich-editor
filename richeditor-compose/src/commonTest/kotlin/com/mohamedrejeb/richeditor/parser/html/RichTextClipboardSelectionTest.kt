package com.mohamedrejeb.richeditor.parser.html

import androidx.compose.ui.text.TextRange
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RichTextClipboardSelectionTest {

    @Test
    fun fullSelectionPreservesParagraphBreaksAndFormatting() {
        val state = RichTextStateHtmlParser.encode(
            "<p>First <strong>bold</strong></p><p>Second line</p>"
        )
        val range = TextRange(0, state.annotatedString.length)

        assertEquals("First bold\nSecond line", state.toText(range))
        assertEquals(
            "<p>First <b>bold</b></p><p>Second line</p>",
            state.toHtml(range),
        )
    }

    @Test
    fun partialSelectionPreservesItsSpanFormatting() {
        val state = RichTextStateHtmlParser.encode(
            "<p>Before <em>selected</em> after</p>"
        )
        val start = state.annotatedString.text.indexOf("selected")
        val range = TextRange(start, start + "selected".length)

        assertEquals("selected", state.toText(range))
        assertEquals("<p><i>selected</i></p>", state.toHtml(range))
    }

    @Test
    fun fullSelectionKeepsAllOrderedListItemsAfterTen() {
        val items = (1..14).joinToString(separator = "") { "<li>Item $it</li>" }
        val state = RichTextStateHtmlParser.encode("<ol>$items</ol>")
        val range = TextRange(0, state.annotatedString.length)
        val html = state.toHtml(range)

        assertTrue(html.contains("<li>Item 10</li>"))
        assertTrue(html.contains("<li>Item 14</li>"))
        assertEquals(state.toHtml(), html)
    }
}
