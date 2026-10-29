package com.andavin.images.placement

import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DisplaySettingsTest {

    @Test
    fun `rejects render distances under one block`() {
        settings(renderDistance = 1, hideDistance = 1)
        assertFailsWith<IllegalArgumentException> { settings(renderDistance = 0, hideDistance = 1) }
    }

    @Test
    fun `rejects hide distances closer than the render distance`() {
        assertFailsWith<IllegalArgumentException> { settings(renderDistance = 32, hideDistance = 31) }
    }
}

private fun settings(renderDistance: Int, hideDistance: Int) =
    DisplaySettings(invisible = false, glowing = false, renderDistance, hideDistance)
