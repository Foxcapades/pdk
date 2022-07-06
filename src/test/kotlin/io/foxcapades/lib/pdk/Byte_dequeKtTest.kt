package io.foxcapades.lib.pdk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

@DisplayName("ByteDeque Helpers")
internal class Byte_dequeKtTest {

  @Nested
  @DisplayName("popUByte()")
  inner class PopUByte {

    @Test
    @DisplayName("correctly parses the bytes for a positive byte value")
    fun t1() {
      val t = ByteDeque(byteArrayOf(127))
      assertEquals(127.toUByte(), t.popUByte())
      assertTrue(t.isEmpty)
    }

    @Test
    @DisplayName("correctly parses the bytes for a negative byte value")
    fun t2() {
      val t = ByteDeque(byteArrayOf(-1))
      assertEquals(255.toUByte(), t.popUByte())
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
      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(1, 0))
        assertEquals(256, t.popShort())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(-128, 0))
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
        val t = ByteDeque(byteArrayOf(0, 1))
        assertEquals(256.toUShort(), t.popUShort(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(0, -128))
        assertEquals(32768.toUShort(), t.popUShort(true))
        assertTrue(t.isEmpty)

        t += -1
        t += -1

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
        val t = ByteDeque(byteArrayOf(1, 0))
        assertEquals(256.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(-128, 0))
        assertEquals(32768.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)

        t += -1
        t += -1

        assertEquals(65535.toUShort(), t.popUShort())
        assertTrue(t.isEmpty)
      }
    }
  }

  @Nested
  @DisplayName("InputStream.read(ByteDeque)")
  inner class InputStreamRead1 {

    @Nested
    @DisplayName("when the given deque has a capacity of 0")
    inner class Cap0 {

      @Test
      @DisplayName("reads 0 bytes")
      fun t1() {
        val inp = ByteArrayInputStream("Hello".toByteArray())
        val tgt = ByteDeque()

        assertEquals(0, inp.read(tgt))
        assertEquals(0, tgt.cap)
        assertEquals(0, tgt.size)
      }
    }

    @Nested
    @DisplayName("when the given deque has 0 space")
    inner class Space0 {

      @Test
      @DisplayName("reads 0 bytes")
      fun t1() {
        val inp = ByteArrayInputStream("Hello".toByteArray())
        val tgt = ByteDeque("hello".toByteArray())

        assertEquals(0, inp.read(tgt))
        assertEquals(5, tgt.cap)
        assertEquals(5, tgt.size)
      }
    }

    @Nested
    @DisplayName("when the given deque is not empty")
    inner class NotEmpty

    @Nested
    @DisplayName("when the given deque is empty")
    inner class Empty

    @Nested
    @DisplayName("when the input stream has no data")
    inner class EmptyStream

  }
}