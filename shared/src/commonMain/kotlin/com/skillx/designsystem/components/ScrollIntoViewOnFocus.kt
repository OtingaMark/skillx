package com.skillx.designsystem.components

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import kotlinx.coroutines.launch

/**
 * Scrolls a field smoothly into view above the keyboard the moment it gains focus — for a
 * field inside a `Modifier.verticalScroll(...)` column, so tabbing between inputs (e.g.
 * Name -> Email -> Password) doesn't leave the next field hidden behind the IME.
 */
@Composable
fun Modifier.scrollIntoViewOnFocus(): Modifier {
    val requester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    return this
        .bringIntoViewRequester(requester)
        .onFocusEvent { state ->
            if (state.isFocused) scope.launch { requester.bringIntoView() }
        }
}
