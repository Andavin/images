package com.andavin.images.placement

import com.andavin.images.palette.MapColor
import java.awt.Color

/**
 * The settings that decide an image's map pixels. With its hash and the
 * client's palette they identify one render, so they're fixed when the image
 * is placed, even when they come from the config's defaults.
 *
 * @property width The width in blocks.
 * @property height The height in blocks.
 * @property fit How the image fills that area.
 * @property background The color behind anything the image doesn't cover.
 * Transparent unless set.
 * @property dither How colors missing from the palette are approximated.
 */
data class RenderSettings(
    val width: Int,
    val height: Int,
    val fit: Fit,
    val background: Color = MapColor.NONE.color,
    val dither: Dither
) {

    init {
        require(width > 0 && height > 0) {
            "An image must be at least 1x1 blocks, not ${width}x$height"
        }
    }
}

/**
 * How an image fills its area.
 */
enum class Fit {

    /**
     * Scale it to fit inside, keeping its shape, with the background around it.
     */
    CONTAIN,

    /**
     * Scale it to cover the whole area, keeping its shape, and crop the overflow.
     */
    COVER,

    /**
     * Scale each side to match the area, changing its shape.
     */
    STRETCH
}

/**
 * How colors missing from the palette are approximated.
 */
enum class Dither {

    /**
     * Each pixel takes the nearest palette color.
     */
    NONE,

    /**
     * Each pixel passes its error on to its neighbors. Best for photos.
     */
    FLOYD_STEINBERG,

    /**
     * Each pixel is nudged by a fixed pattern. Stays stable across GIF frames.
     */
    ORDERED
}
