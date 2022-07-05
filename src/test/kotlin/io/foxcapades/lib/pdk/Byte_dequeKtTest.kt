package io.foxcapades.lib.pdk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ByteDeque Helpers")
internal class Byte_dequeKtTest {

  @Nested
  @DisplayName("popShort(Boolean)")
  inner class PopShort1 {

    @Nested
    @DisplayName("when littleEndian is true")
    inner class LittleEndian {

      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(0, 1))
        assertEquals(256, t.popShort(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(0, -128))
        assertEquals(-32768, t.popShort(true))
        assertTrue(t.isEmpty)
      }
    }

    @Nested
    @DisplayName("when littleEndian is false")
    inner class BigEndian {

    }
  }
}