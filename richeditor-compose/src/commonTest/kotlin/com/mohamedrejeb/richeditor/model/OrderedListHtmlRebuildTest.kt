package com.mohamedrejeb.richeditor.model

import kotlin.test.Test
import kotlin.test.assertContains

class OrderedListHtmlRebuildTest {

    @Test
    fun htmlOrderedListKeepsContentAfterDoubleDigitMarkers() {
        assertHtmlOrderedListKeepsContentAfterDoubleDigitMarkers("item")
    }

    @Test
    fun htmlOrderedListKeepsMultibyteContentAfterDoubleDigitMarkers() {
        assertHtmlOrderedListKeepsContentAfterDoubleDigitMarkers("电脑")
    }

    private fun assertHtmlOrderedListKeepsContentAfterDoubleDigitMarkers(itemPrefix: String) {
        val html = buildString {
            append("<ol>")
            for (index in 1..13) {
                append("<li>")
                append(itemPrefix)
                append(index)
                append("</li>\n")
            }
            append("</ol>")
        }

        val state = RichTextState()
        state.setHtml(html)
        state.updateAnnotatedString()

        for (index in 1..13) {
            assertContains(state.annotatedString.text, "$index. $itemPrefix$index")
        }
    }
}
