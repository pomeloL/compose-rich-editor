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

        val selectionSnapshot = richTextState.clipboardSelectionSnapshot()
        if (selectionSnapshot == null) {
            clipboard.setClipEntry(clipEntry)
            return
        }

        runCatching {
            val richClipData = ClipData.newHtmlText(
                "rich text",
                selectionSnapshot.plainText,
                selectionSnapshot.htmlText,
            )
            clipboard.setClipEntry(ClipEntry(richClipData))
        }.getOrElse {
            clipboard.setClipEntry(clipEntry)
        }
    }
}
