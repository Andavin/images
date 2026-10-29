package com.andavin.images

import java.security.MessageDigest

/**
 * The SHA-256 of an image's original bytes. It finds those bytes in storage,
 * and the same picture has the same hash however many times it's placed.
 */
class CustomImageHash(digest: ByteArray) {

    private val bytes = digest.copyOf()

    init {
        require(bytes.size == SHA_256_BYTES) {
            "A SHA-256 digest is $SHA_256_BYTES bytes, not ${bytes.size}"
        }
    }

    override fun equals(other: Any?) = other is CustomImageHash && bytes.contentEquals(other.bytes)

    override fun hashCode() = bytes.contentHashCode()

    override fun toString() = bytes.joinToString("") { "%02x".format(it) }

    companion object {

        private const val SHA_256_BYTES = 32

        fun of(data: ByteArray) = CustomImageHash(MessageDigest.getInstance("SHA-256").digest(data))
    }
}
