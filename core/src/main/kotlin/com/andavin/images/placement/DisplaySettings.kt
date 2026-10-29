package com.andavin.images.placement

/**
 * How an image shows to players. None of it changes the map pixels, so the
 * config's defaults apply live to every image that doesn't set its own
 * [DisplayOverrides].
 *
 * @property invisible Whether the item frames are hidden, leaving only the maps.
 * @property glowing Whether the maps show at full brightness in glow item frames.
 * @property renderDistance How close, in blocks, a player must be to see it.
 * @property hideDistance How far, in blocks, a player must go before it's
 * hidden again. Keeping this past [renderDistance] stops it flickering at the edge.
 */
data class DisplaySettings(
    val invisible: Boolean,
    val glowing: Boolean,
    val renderDistance: Int,
    val hideDistance: Int
) {

    init {
        require(renderDistance > 0) {
            "The render distance must be at least 1 block, not $renderDistance"
        }
        require(hideDistance >= renderDistance) {
            "The hide distance ($hideDistance) can't be closer than the render distance ($renderDistance)"
        }
    }
}

/**
 * An image's own display settings. Anything left null follows the config's
 * default, so changing a default reaches every image that didn't set its own.
 */
data class DisplayOverrides(
    val invisible: Boolean? = null,
    val glowing: Boolean? = null,
    val renderDistance: Int? = null,
    val hideDistance: Int? = null
) {

    init {
        require(renderDistance == null || renderDistance > 0) {
            "The render distance must be at least 1 block, not $renderDistance"
        }
        require(hideDistance == null || hideDistance > 0) {
            "The hide distance must be at least 1 block, not $hideDistance"
        }
        require(renderDistance == null || hideDistance == null || hideDistance >= renderDistance) {
            "The hide distance ($hideDistance) can't be closer than the render distance ($renderDistance)"
        }
    }

    /**
     * Fill in everything this doesn't set from [defaults]. When only one
     * distance is set here, the default one moves to keep the hide
     * distance at or past the render distance.
     */
    fun resolve(defaults: DisplaySettings): DisplaySettings {
        val render = renderDistance ?: minOf(defaults.renderDistance, hideDistance ?: Int.MAX_VALUE)
        return DisplaySettings(
            invisible ?: defaults.invisible,
            glowing ?: defaults.glowing,
            render,
            hideDistance ?: maxOf(defaults.hideDistance, render)
        )
    }
}
