package com.andavin.images

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class CustomImageHashTest {

    @Test
    fun `hashes bytes with SHA-256`() {
        assertEquals(ABC_SHA_256, CustomImageHash.of("abc".toByteArray()).toString())
    }

    @Test
    fun `equals the hash of the same bytes`() {
        val hash = CustomImageHash.of(byteArrayOf(1, 2, 3))
        assertEquals(CustomImageHash.of(byteArrayOf(1, 2, 3)), hash)
        assertEquals(CustomImageHash.of(byteArrayOf(1, 2, 3)).hashCode(), hash.hashCode())
        assertNotEquals(CustomImageHash.of(byteArrayOf(3, 2, 1)), hash)
    }

    @Test
    fun `rejects digests that are not 32 bytes`() {
        assertFailsWith<IllegalArgumentException> { CustomImageHash(ByteArray(31)) }
        assertFailsWith<IllegalArgumentException> { CustomImageHash(ByteArray(33)) }
    }
}

// The SHA-256 of "abc" from FIPS 180-2, appendix B.1
private const val ABC_SHA_256 = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
