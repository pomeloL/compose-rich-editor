package com.mohamedrejeb.richeditor.ui

import androidx.compose.ui.platform.Clipboard
import com.mohamedrejeb.richeditor.model.RichTextState

internal actual fun createPlatformRichTextClipboard(
    richTextState: RichTextState,
    clipboard: Clipboard,
): Clipboard = clipboard
