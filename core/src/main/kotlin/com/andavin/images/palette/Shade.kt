package com.andavin.images.palette

import java.awt.Color

/**
 * How bright a [MapColor] shows on a map, from vanilla's
 * `MapColor.Brightness` and in its ID order. Vanilla's terrain drawing
 * (`MapItem.update`) picks [LOW], [NORMAL] or [HIGH] by comparing each
 * block's height with the block north of it. It only uses [LOWEST] for
 * explorer maps' biome previews, but clients show it anywhere.
 *
 * See https://minecraft.wiki/w/Map_item_format#Map_colors.
 */
internal enum class Shade(private val brightness: Int) {

    LOW(180),
    NORMAL(220),
    HIGH(FULL_BRIGHTNESS),
    LOWEST(135);

    /**
     * Scale each channel of [color] by [brightness] out of [FULL_BRIGHTNESS],
     * rounding down, the same as vanilla's `ARGB.scaleRGB`.
     */
    fun apply(color: Color) = Color(
        scale(color.red),
        scale(color.green),
        scale(color.blue),
        color.alpha
    )

    private fun scale(channel: Int) = channel * brightness / FULL_BRIGHTNESS
}

private const val FULL_BRIGHTNESS = 255
