package com.andavin.images.palette

/**
 * The map colors a client can display, named after the first version that
 * uses each set. Each palette adds base colors to the end of the one before
 * it, up to and including the last one it's given.
 *
 * @param last The final map color in this palette.
 */
enum class Palette(last: MapColor) {

    V1_8(MapColor.NETHER),
    V1_12(MapColor.TERRACOTTA_BLACK),
    V1_16(MapColor.WARPED_WART_BLOCK),
    V1_17(MapColor.GLOW_LICHEN);

    // Indexed by map color ID, which vanilla packs as base ID × 4 + shade ID
    private val colors = MapColor.entries.take(last.ordinal + 1)
        .flatMap { base -> Shade.entries.map { it.apply(base.color).rgb } }
        .toIntArray()

    /**
     * Find the map color ID closest to [argb], using the same weighted
     * distance as Bukkit's `MapPalette.matchColor`. Pixels under half
     * opacity are transparent.
     */
    fun match(argb: Int): Byte {

        if (argb.alpha < MIN_OPAQUE_ALPHA) {
            return TRANSPARENT
        }

        var best = FIRST_COLOR_ID
        var bestDistance = Int.MAX_VALUE
        for (id in FIRST_COLOR_ID until colors.size) {
            val distance = distance(argb, colors[id])
            if (distance < bestDistance) {
                best = id
                bestDistance = distance
            }
        }

        return best.toByte()
    }

    /**
     * Match each ARGB pixel to its map color ID, keeping the order.
     */
    fun match(pixels: IntArray): ByteArray = ByteArray(pixels.size) {
        match(pixels[it])
    }
}

private const val TRANSPARENT: Byte = 0

// Bukkit's MapPalette.matchColor treats anything less opaque as transparent
private const val MIN_OPAQUE_ALPHA = 128

// The first ID after MapColor.NONE's shades, which are all transparent
private val FIRST_COLOR_ID = (MapColor.NONE.ordinal + 1) * Shade.entries.size

/**
 * Bukkit's `MapPalette.getDistance`: the "redmean" distance from
 * https://www.compuphase.com/cmetric.htm ("A low-cost approximation"),
 * (2 + r̄/256)ΔR² + 4ΔG² + (2 + (255 - r̄)/256)ΔB² where r̄ is the mean red.
 * It leaves out the square root, which never changes which color is closest.
 */
private fun distance(a: Int, b: Int): Int {
    val redSum = a.red + b.red
    val redWeight = BASE_WEIGHT + redSum
    val blueWeight = BASE_WEIGHT + MAX_RED_SUM - redSum
    val red = a.red - b.red
    val green = a.green - b.green
    val blue = a.blue - b.blue
    return redWeight * red * red + GREEN_WEIGHT * green * green + blueWeight * blue * blue
}

// Multiplying every weight by 512 turns r̄/256 into the sum of the two reds,
// so the whole distance stays in integers without changing the result
private const val WEIGHT_SCALE = 512
private const val BASE_WEIGHT = 2 * WEIGHT_SCALE
private const val GREEN_WEIGHT = 4 * WEIGHT_SCALE
private const val MAX_RED_SUM = 2 * 255

// The channels of a packed 0xAARRGGBB color, as Color.getRGB and BufferedImage.getRGB return
private val Int.alpha get() = this ushr 24
private val Int.red get() = this shr 16 and 0xFF
private val Int.green get() = this shr 8 and 0xFF
private val Int.blue get() = this and 0xFF
