package com.mohamedrejeb.richeditor.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.paragraph.RichParagraph
import kotlin.test.Test
import kotlin.test.assertEquals

/** Regression coverage for the selection-dependent background mask from issue #635. */
@OptIn(ExperimentalRichTextApi::class)
class Issue635BackgroundOnSelectionTest {

    private fun stateWithText(text: String): RichTextState =
        RichTextState(
            listOf(
                RichParagraph(key = 1).also { paragraph ->
                    paragraph.children.add(
                        RichSpan(text = text, paragraph = paragraph),
                    )
                }
            )
        )

    private fun backgroundAt(state: RichTextState, offset: Int): Color {
        var background: Color = Color.Unspecified
        state.annotatedString.spanStyles
            .filter { offset in it.start until it.end }
            .forEach { range ->
                if (range.item.background != Color.Unspecified) {
                    background = range.item.background
                }
            }
        return background
    }

    @Test
    fun backgroundIsMaskedWhileSelectionCoversIt() {
        val state = stateWithText("Hello World")

        state.selection = TextRange(0, 5)
        state.addSpanStyle(SpanStyle(background = Color.Yellow))

        assertEquals(Color.Transparent, backgroundAt(state, offset = 2))
    }

    @Test
    fun backgroundReappearsWhenSelectionCollapsesWithoutTyping() {
        val state = stateWithText("Hello World")

        state.selection = TextRange(0, 5)
        state.addSpanStyle(SpanStyle(background = Color.Yellow))
        state.onTextFieldValueChange(
            TextFieldValue(
                text = state.annotatedString.text,
                selection = TextRange(2),
            )
        )

        assertEquals(Color.Yellow, backgroundAt(state, offset = 2))
    }

    @Test
    fun backgroundReappearsWhenSelectionMovesAwayFromPaintedRange() {
        val state = stateWithText("Hello World")

        state.selection = TextRange(0, 5)
        state.addSpanStyle(SpanStyle(background = Color.Yellow))
        state.onTextFieldValueChange(
            TextFieldValue(
                text = state.annotatedString.text,
                selection = TextRange(6, 11),
            )
        )

        assertEquals(Color.Yellow, backgroundAt(state, offset = 2))
    }
}
