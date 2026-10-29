package com.andavin.images

import com.andavin.images.placement.DisplayOverrides
import com.andavin.images.placement.RenderSettings
import com.andavin.images.util.BlockPosition
import com.andavin.images.util.Facing
import com.andavin.images.util.Rotation
import java.time.Instant
import java.util.UUID

/**
 * Stands in for a player's UUID when the console places an image.
 * It's the nil UUID, which no player has.
 */
val CONSOLE_ID: UUID = UUID(0, 0)

/**
 * An image placed in a world.
 *
 * @property id Identifies the image across every server sharing the storage.
 * @property hash The hash of its original bytes, which finds them in storage.
 * @property server The server the world belongs to.
 * @property world The world's UUID.
 * @property origin The block holding the image's top-left item frame.
 * @property facing The way the image faces, out from the surface it's on.
 * @property rotation How far the image is turned on that surface.
 * @property render The settings that decide its map pixels.
 * @property display Its own display settings. The rest follow the config.
 * @property owner The player who placed it, or [CONSOLE_ID].
 * @property placedAt When it was placed.
 * @property approval Whether it has been approved to show.
 */
data class CustomImage(
    val id: UUID,
    val hash: CustomImageHash,
    val server: UUID,
    val world: UUID,
    val origin: BlockPosition,
    val facing: Facing,
    val rotation: Rotation,
    val render: RenderSettings,
    val display: DisplayOverrides,
    val owner: UUID,
    val placedAt: Instant,
    val approval: Approval
)

/**
 * Whether an image can show yet. Only used when the approval queue is on.
 */
enum class Approval {

    /**
     * Waiting for staff, and hidden from everyone but its owner and staff.
     */
    PENDING,

    /**
     * Showing to everyone.
     */
    APPROVED
}
