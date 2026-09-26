package com.skillx.core.result

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppResultTest {
    @Test fun successResult() {
        val result: AppResult<String, String> = AppResult.Success("ok")
        assertTrue(result.isSuccess); assertEquals("ok", result.getOrNull())
    }
    @Test fun errorResult() {
        val result: AppResult<String, String> = AppResult.Error("fail")
        assertTrue(result.isError); assertNull(result.getOrNull()); assertEquals("fail", result.errorOrNull())
    }
    @Test fun mapTransformsSuccess() {
        val result: AppResult<Int, String> = AppResult.Success(5)
        val mapped = result.map { it * 2 }
        assertEquals(10, mapped.getOrNull())
    }
    @Test fun mapPreservesError() {
        val result: AppResult<Int, String> = AppResult.Error("fail")
        val mapped = result.map { it * 2 }
        assertTrue(mapped.isError); assertEquals("fail", mapped.errorOrNull())
    }
}
