package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OrderedListIndentTest {

    private fun config(indent: Int): RichTextConfig =
        RichTextConfig(updateText = {}).apply {
            orderedListIndent = indent
            unorderedListIndent = indent
        }

    @Test
    fun orderedIndentLargerThanPrefixAlignsDots() {
        val config = config(indent = 38)
        val list = OrderedList(number = 1, config = config, startTextWidth = 20.sp)

        val textIndent = list.getStyle(config).textIndent
        assertNotNull(textIndent)

        assertEquals(18f, textIndent.firstLine.value)
        assertEquals(38f, textIndent.restLine.value)
        assertTrue(textIndent.firstLine.value < textIndent.restLine.value)
    }

    @Test
    fun orderedIndentSmallerThanPrefixKeepsPrefixVisible() {
        val config = config(indent = 10)
        val list = OrderedList(number = 1, config = config, startTextWidth = 50.sp)

        val textIndent = list.getStyle(config).textIndent
        assertNotNull(textIndent)

        assertEquals(10f, textIndent.firstLine.value)
        assertEquals(60f, textIndent.restLine.value)
        assertTrue(textIndent.firstLine.value >= 0f)
    }

    @Test
    fun orderedZeroIndentKeepsPrefixVisible() {
        val config = config(indent = 0)
        val list = OrderedList(number = 1, config = config, startTextWidth = 20.sp)

        val textIndent = list.getStyle(config).textIndent
        assertNotNull(textIndent)

        assertEquals(0f, textIndent.firstLine.value)
        assertEquals(20f, textIndent.restLine.value)
    }

    @Test
    fun orderedDeeperLevelGrowsTheGutterAndRestoresDotAlignment() {
        val config = config(indent = 10)

        val listLevel2 = OrderedList(number = 1, config = config, startTextWidth = 50.sp, initialLevel = 2)
        val level2Indent = listLevel2.getStyle(config).textIndent!!
        assertEquals(20f, level2Indent.firstLine.value)
        assertEquals(70f, level2Indent.restLine.value)

        val listLevel6 = OrderedList(number = 1, config = config, startTextWidth = 50.sp, initialLevel = 6)
        val level6Indent = listLevel6.getStyle(config).textIndent!!
        assertEquals(10f, level6Indent.firstLine.value)
        assertEquals(60f, level6Indent.restLine.value)
    }

    @Test
    fun unorderedIndentLargerThanPrefixAlignsMarkerInGutter() {
        val config = config(indent = 38)
        val list = UnorderedList(config = config).apply { startTextWidth = 12.sp }

        val textIndent = list.getStyle(config).textIndent
        assertNotNull(textIndent)

        assertEquals(26f, textIndent.firstLine.value)
        assertEquals(38f, textIndent.restLine.value)
    }

    @Test
    fun unorderedZeroIndentKeepsPrefixVisible() {
        val config = config(indent = 0)
        val list = UnorderedList(config = config).apply { startTextWidth = 12.sp }

        val textIndent = list.getStyle(config).textIndent
        assertNotNull(textIndent)

        assertEquals(0f, textIndent.firstLine.value)
        assertEquals(12f, textIndent.restLine.value)
    }
}
