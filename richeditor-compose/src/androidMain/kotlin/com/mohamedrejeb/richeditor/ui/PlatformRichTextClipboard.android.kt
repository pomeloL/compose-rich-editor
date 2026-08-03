package com.mohamedrejeb.richeditor.ui

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import com.mohamedrejeb.richeditor.model.RichTextState

internal actual fun createPlatformRichTextClipboard(
    richTextState: RichTextState,
    clipboard: Clipboard,
): Clipboard = AndroidRichTextClipboard(
    richTextState = richTextState,
    clipboard = clipboard,
)

private class AndroidRichTextClipboard(
    private val richTextState: RichTextState,
    private val clipboard: Clipboard,
) : Clipboard by clipboard {

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        if (clipEntry == null) {
            clipboard.setClipEntry(null)
            return
        }

        val selection = richTextState.copySelection
        if (selection == null || selection.collapsed) {
            clipboard.setClipEntry(clipEntry)
            return
        }

        runCatching {
            val plainText = richTextState.toText(selection)
            val htmlText = richTextState.toHtml(selection)
            val richClipData = ClipData.newHtmlText("rich text", plainText, htmlText)
            clipboard.setClipEntry(ClipEntry(richClipData))
        }.getOrElse {
            clipboard.setClipEntry(clipEntry)
        }
    }
}
