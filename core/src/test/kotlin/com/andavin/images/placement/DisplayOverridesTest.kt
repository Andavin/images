package com.andavin.images.placement

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DisplayOverridesTest {

    @Test
    fun `uses the defaults for anything it doesn't set`() {
        assertEquals(DEFAULTS, DisplayOverrides().resolve(DEFAULTS))
    }

    @Test
    fun `uses its own values over the defaults`() {
        val overrides = DisplayOverrides(invisible = true, glowing = true, renderDistance = 16, hideDistance = 24)
        assertEquals(DisplaySettings(true, true, 16, 24), overrides.resolve(DEFAULTS))
    }

    @Test
    fun `moves the default hide distance out past its own render distance`() {
        val resolved = DisplayOverrides(renderDistance = 100).resolve(DEFAULTS)
        assertEquals(100, resolved.renderDistance)
        assertEquals(100, resolved.hideDistance)
    }

    @Test
    fun `moves the default render distance in to its own hide distance`() {
        val resolved = DisplayOverrides(hideDistance = 16).resolve(DEFAULTS)
        assertEquals(16, resolved.renderDistance)
        assertEquals(16, resolved.hideDistance)
    }

    @Test
    fun `rejects distances that could never be valid`() {
        assertFailsWith<IllegalArgumentException> { DisplayOverrides(renderDistance = 0) }
        assertFailsWith<IllegalArgumentException> { DisplayOverrides(hideDistance = 0) }
        assertFailsWith<IllegalArgumentException> { DisplayOverrides(renderDistance = 32, hideDistance = 16) }
    }
}

private val DEFAULTS = DisplaySettings(invisible = false, glowing = false, renderDistance = 32, hideDistance = 48)
