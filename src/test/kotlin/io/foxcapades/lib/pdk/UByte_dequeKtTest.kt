
package io.foxcapades.lib.pdk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UByteDeque Helpers")
@OptIn(ExperimentalUnsignedTypes::class)
internal class UByte_dequeKtTest {

  @Nested
  @DisplayName("popByte()")
  inner class PopByte {

    @Test
    @DisplayName("correctly parses the bytes for a positive byte value")
    fun t1() {
      val t = UByteDeque(ubyteArrayOf(128u))
      assertEquals((-128).toByte(), t.popByte())
      assertTrue(t.isEmpty)
    }

    @Test
    @DisplayName("correctly parses the bytes for a negative byte value")
    fun t2() {
      val t = UByteDeque(ubyteArrayOf(255u))
      assertEquals((-1).toByte(), t.popByte())
      assertTrue(t.isEmpty)
    }
  }

  @Nested
  @DisplayName("popShort(Boolean)")
  inner class PopShort1 {

    @Nested
    @DisplayName("when littleEndian is true")
    inner class LittleEndian {

      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = UByteDeque(ubyteArrayOf(0u, 1u))
        assertEquals(256, t.popShort(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = UByteDeque(ubyteArrayOf(0u, 128u))
        assertEquals(-32768, t.popShort(true))
        assertTrue(t.isEmpty)
      }
    }

    @Nested
    @DisplayName("when littleEndian is false")
    inner class BigEndian {
      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = UByteDeque(ubyteArrayOf(1u, 0u))
        assertEquals(256, t.popShort())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = UByteDeque(ubyteArrayOf(128u, 0u))
        assertEquals(-32768, t.popShort())
        assertTrue(t.isEmpty)
      }
    }
  }

  @Nested
  @DisplayName("popUShort(Boolean)")
  inner class PopUShort1 {

    @Nested
    @DisplayName("when littleEndian is true")
    inner class LittleEndian {

      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = UByteDeque(ubyteArrayOf(0u, 1u))
        assertEquals(256.toUShort(), t.popUShort(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = UByteDeque(ubyteArrayOf(0u, 128u))
        assertEquals(32768.toUShort(), t.popUShort(true))
        assertTrue(t.isEmpty)

        t += 255u
        t += 255u

        assertEquals(65535.toUShort(), t.popUShort(true))
        assertTrue(t.isEmpty)
      }
    }

    @Nested
    @DisplayName("when littleEndian is false")
    inner class BigEndian {
      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = UByteDeque(ubyteArrayOf(1u, 0u))
        assertEquals(256.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = UByteDeque(ubyteArrayOf(128u, 0u))
        assertEquals(32768.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)

        t += 255u
        t += 255u

        assertEquals(65535.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)
      }
    }
  }
}