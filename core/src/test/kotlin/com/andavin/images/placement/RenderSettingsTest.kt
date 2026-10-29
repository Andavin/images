package com.andavin.images.placement

import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class RenderSettingsTest {

    @Test
    fun `rejects sizes under one block`() {
        settings(width = 1, height = 1)
        assertFailsWith<IllegalArgumentException> { settings(width = 0) }
        assertFailsWith<IllegalArgumentException> { settings(height = 0) }
    }
}

private fun settings(width: Int = 1, height: Int = 1) =
    RenderSettings(width, height, Fit.CONTAIN, dither = Dither.NONE)
