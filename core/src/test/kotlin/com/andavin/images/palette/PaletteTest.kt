package com.andavin.images.palette

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaletteTest {

    @ParameterizedTest
    @EnumSource(Palette::class)
    fun `matches each of its colors to that color's ID`(palette: Palette) {
        for (id in FIRST_COLOR_ID until palette.size) {
            assertEquals(id, palette.id(OPAQUE or BUKKIT_COLORS[id]), "ID $id")
        }
    }

    @ParameterizedTest
    @EnumSource(Palette::class)
    fun `never matches colors added in later versions`(palette: Palette) {
        for (rgb in BUKKIT_COLORS.drop(FIRST_COLOR_ID)) {
            val id = palette.id(OPAQUE or rgb)
            assertTrue(id < palette.size, "%06X matched ID $id".format(rgb))
        }
    }

    @ParameterizedTest
    @EnumSource(Palette::class)
    fun `matches pixels under half opacity to transparent`(palette: Palette) {
        assertEquals(TRANSPARENT, palette.id(WHITE))
        assertEquals(TRANSPARENT, palette.id(ALMOST_HALF_OPAQUE or WHITE))
        assertEquals(SNOW_HIGH, palette.id(HALF_OPAQUE or WHITE))
    }

    @Test
    fun `matches colors to the same IDs as Bukkit`() {
        for (line in resource("/palette/bukkit-matches.txt")) {
            val columns = line.split(' ')
            val rgb = columns[0].toInt(16)
            Palette.entries.forEachIndexed { index, palette ->
                assertEquals(
                    columns[index + 1].toInt(),
                    palette.id(OPAQUE or rgb),
                    "${columns[0]} on $palette"
                )
            }
        }
    }

    @Test
    fun `matches every pixel in order`() {
        val pixels = intArrayOf(WHITE, OPAQUE or GRASS, OPAQUE or WHITE, OPAQUE or GLOW_LICHEN)
        assertEquals(
            listOf(TRANSPARENT, GRASS_HIGH, SNOW_HIGH, GLOW_LICHEN_HIGH),
            Palette.V1_17.match(pixels).map { it.unsigned }
        )
    }
}

// Alpha in the top byte of a packed 0xAARRGGBB color
private const val OPAQUE = 0xFF shl 24
private const val HALF_OPAQUE = 0x80 shl 24
private const val ALMOST_HALF_OPAQUE = 0x7F shl 24

private const val WHITE = 0xFFFFFF
private const val GRASS = 0x7FB238
private const val GLOW_LICHEN = 0x7FA796

// Map color IDs in Bukkit's table, each base ID × 4 + shade ID
private const val TRANSPARENT = 0
private const val FIRST_COLOR_ID = 4
private const val GRASS_HIGH = 6
private const val SNOW_HIGH = 34
private const val GLOW_LICHEN_HIGH = 246

private val BUKKIT_COLORS = resource("/palette/bukkit-colors.txt").map {
    if (it == "transparent") 0 else it.toInt(16)
}

// The length of Bukkit's MapPalette.colors in 1.8.8, 1.12.2, 1.16.5 and 1.17.1
private val Palette.size: Int
    get() = when (this) {
        Palette.V1_8 -> 144
        Palette.V1_12 -> 208
        Palette.V1_16 -> 236
        Palette.V1_17 -> 248
    }

private val Byte.unsigned get() = toInt() and 0xFF

private fun Palette.id(argb: Int) = match(argb).unsigned

private fun resource(name: String) = PaletteTest::class.java.getResource(name)!!.readText()
    .lines()
    .filter { it.isNotBlank() && !it.startsWith("#") }
