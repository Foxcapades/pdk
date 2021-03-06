package io.foxcapades.lib.pdk

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@DisplayName("ByteQueue")
internal class ByteDequeTest {

  fun createWonky(size: Int, byteArray: ByteArray): ByteDeque {
    val out = ByteDeque(size)

    for (i in byteArray.indices.reversed()) {
      out.pushHead(byteArray[i])
    }

    return out
  }

  @Nested
  @DisplayName("size")
  inner class Size {

    @Nested
    @DisplayName("when the deque is empty")
    inner class Empty {

      @Nested
      @DisplayName("and the capacity is 0")
      inner class Cap0 {

        @Test
        @DisplayName("returns 0")
        fun t1() = assertEquals(0, ByteDeque(0).size)
      }

      @Nested
      @DisplayName("and the capacity is greater than 0")
      inner class Cap1 {

        @Test
        @DisplayName("returns 0")
        fun t1() = assertEquals(0, ByteDeque(10).size)
      }
    }

    @Nested
    @DisplayName("when the deque is not empty")
    inner class NotEmpty {

      @Test
      @DisplayName("returns the number of elements in the deque")
      fun t1() = assertEquals(3, ByteDeque.of(0, 1, 2).size)
    }
  }

  @Nested
  @DisplayName("cap")
  inner class Cap {

  }

  @Nested
  @DisplayName("isEmpty")
  inner class IsEmpty

  @Nested
  @DisplayName("lastIndex")
  inner class LastIndex

  @Nested
  @DisplayName("clear()")
  inner class Clear

  @Nested
  @DisplayName("reset()")
  inner class Reset

  @Nested
  @DisplayName("copy()")
  inner class Copy

  @Nested
  @DisplayName("ensureCapacity()")
  inner class EnsureCapacity

  @Nested
  @DisplayName("toArray()")
  inner class ToArray {
    @Test
    fun t1() {
      val tgt = ByteDeque(10)
      tgt.push(1)
      tgt.push(2)
      tgt.push(3)

      val out = tgt.toArray()

      assertEquals(3, out.size)
      assertEquals(3, out[0])
      assertEquals(2, out[1])
      assertEquals(1, out[2])
    }
  }

  @Nested
  @DisplayName("toList()")
  inner class ToList

  @Nested
  @DisplayName("copyInto()")
  inner class CopyInto {

    @Nested
    @DisplayName("with no offset")
    inner class NoOffset {

      @Nested
      @DisplayName("and an input capacity less than the deque size")
      inner class SmallerIn {

        @Test
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          val inp = ByteArray(5)

          tgt.copyInto(inp)

          assertEquals("hello", String(inp))
        }

      }

      @Nested
      @DisplayName("and an input capacity equal to the deque size")
      inner class SameIn {
        @Test
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          val inp = ByteArray(11)

          tgt.copyInto(inp)

          assertEquals("hello world", String(inp))
        }
      }

      @Nested
      @DisplayName("and an input capacity greater than the deque size")
      inner class BiggerIn {
        @Test
        fun t1() {
          val tgt = ByteDeque(11)
          tgt.push('o'.code.toByte())
          tgt.push('l'.code.toByte())
          tgt.push('l'.code.toByte())
          tgt.push('e'.code.toByte())
          tgt.push('h'.code.toByte())
          tgt.pushBack(' '.code.toByte())
          tgt.pushBack('w'.code.toByte())
          tgt.pushBack('o'.code.toByte())
          tgt.pushBack('r'.code.toByte())
          tgt.pushBack('l'.code.toByte())
          tgt.pushBack('d'.code.toByte())

          val inp = ByteArray(20) { 61 }

          tgt.copyInto(inp)

          assertEquals("hello world=========", String(inp))
        }
      }

    }

    @Nested
    @DisplayName("with an offset")
    inner class WithOffset {

    }
  }

  @Nested
  @DisplayName("iterator()")
  inner class Iterator

  @Nested
  @DisplayName("head")
  inner class Head1

  @Nested
  @DisplayName("first")
  inner class First1

  @Nested
  @DisplayName("front")
  inner class Front1

  @Nested
  @DisplayName("head()")
  inner class Head2

  @Nested
  @DisplayName("first()")
  inner class First2

  @Nested
  @DisplayName("front()")
  inner class Front2

  @Nested
  @DisplayName("pop()")
  inner class Pop

  @Nested
  @DisplayName("popHead()")
  inner class PopHead

  @Nested
  @DisplayName("popFirst()")
  inner class PopFirst

  @Nested
  @DisplayName("popFront()")
  inner class PopFront

  @Nested
  @DisplayName("removeHead()")
  inner class RemoveHead

  @Nested
  @DisplayName("removeFirst()")
  inner class RemoveFirst

  @Nested
  @DisplayName("removeFront()")
  inner class RemoveFront

  @Nested
  @DisplayName("deleteHead()")
  inner class DeleteHead

  @Nested
  @DisplayName("deleteFirst()")
  inner class DeleteFirst

  @Nested
  @DisplayName("deleteFront()")
  inner class DeleteFront

  @Nested
  @DisplayName("push")
  inner class Push

  @Nested
  @DisplayName("pushHead")
  inner class PushHead

  @Nested
  @DisplayName("pushFirst")
  inner class PushFirst

  @Nested
  @DisplayName("pushFront")
  inner class PushFront

  @Nested
  @DisplayName("plus")
  inner class Plus {

    @Test
    fun t1() {
      val t1 = ByteDeque("hello".toByteArray())
      val t2 = ByteDeque(" ".toByteArray())
      val t3 = ByteDeque("world".toByteArray())
      val t4 = t1 + t2 + t3

      assertEquals("hello world", String(t4.toArray()))
    }
  }

  @Nested
  @DisplayName("pushTail(ByteArray)")
  inner class PushTail2 {

    @Nested
    @DisplayName("when the deque is empty")
    inner class Empty {

      @Nested
      @DisplayName("and the input array is empty")
      inner class Empty {

        @Test
        @DisplayName("does nothing")
        fun t1() {
          val t = ByteDeque()
          t.pushTail(ByteArray(0))
          assertTrue(t.isEmpty)
        }
      }

      @Nested
      @DisplayName("and the input array is not empty")
      inner class NonEmpty {

        @Test
        @DisplayName("copies the contents of the input array")
        fun t1() {
          val t = ByteDeque()
          t.pushTail("h".toByteArray())
          assertContentEquals("h".toByteArray(), t.toArray())
        }
      }
    }

    @Nested
    @DisplayName("when the deque already has the new required capacity")
    inner class HasCap

    @Nested
    @DisplayName("when the deque has less than the new required capacity")
    inner class NeedsCap

    @Test
    @DisplayName("when the deque already has the required capacity")
    fun t1() {
      // make a deque, presized to fit everything
      // push stuff onto the front of it to make it out of order
      // push the byte array
      val t1 = ByteDeque(100)
      t1.pushHead('o'.code.toByte())
      t1.pushHead('l'.code.toByte())
      t1.pushHead('l'.code.toByte())
      t1.pushHead('e'.code.toByte())
      t1.pushHead('h'.code.toByte())
      t1.pushTail(' '.code.toByte())

      t1.pushTail("world".toByteArray())

      assertEquals("hello world", String(t1.toArray()))
    }

    @Test
    @DisplayName("when the deque needs to resize to the required capacity")
    fun t2() {
      // make a deque
      // push stuff onto the front of it to make it out of order
      // push the byte array
      val t1 = ByteDeque(6)
      t1.pushHead('o'.code.toByte())
      t1.pushHead('l'.code.toByte())
      t1.pushHead('l'.code.toByte())
      t1.pushHead('e'.code.toByte())
      t1.pushHead('h'.code.toByte())
      t1.pushTail(' '.code.toByte())

      t1.pushTail("world".toByteArray())

      assertEquals("hello world", String(t1.toArray()))
    }
  }

  @Nested
  @DisplayName("slice(Int, Int)")
  inner class Slice1 {

    @Nested
    @DisplayName("when the inputs are invalid")
    inner class InvalidInputs {

      @Nested
      @DisplayName("due to the start index being less than 0")
      inner class StartLT0 {

        @Test
        @DisplayName("throws an IndexOutOfBoundsException")
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          assertFailsWith<IndexOutOfBoundsException> { tgt.slice(-1) }
        }
      }

      @Nested
      @DisplayName("due to the start index being greater than or equal to the deque's size")
      inner class StartGTEQSize {

        @Test
        @DisplayName("throws an IndexOutOfBoundsException")
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          assertFailsWith<IndexOutOfBoundsException> { tgt.slice(tgt.size) }
          assertFailsWith<IndexOutOfBoundsException> { tgt.slice(tgt.size+1, tgt.size+2) }
        }
      }

      @Nested
      @DisplayName("due to the start index being greater than the end index")
      inner class StartGTEnd {

        @Test
        @DisplayName("throws an IndexOutOfBoundsException")
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          assertFailsWith<IndexOutOfBoundsException> { tgt.slice(2, 1) }
        }
      }

      @Nested
      @DisplayName("due to the end index being greater than the deque's size")
      inner class EndGTSize {

        @Test
        @DisplayName("throws an IndexOutOfBoundsException")
        fun t1() {
          val tgt = ByteDeque("hello world".toByteArray())
          assertFailsWith<IndexOutOfBoundsException> { tgt.slice(0, 100) }
        }
      }
    }

    @Nested
    @DisplayName("when the inputs are valid")
    inner class ValidInputs {

      @Nested
      @DisplayName("the deque is inline")
      inner class Inline {

        val tgt get() = ByteDeque("hello world".toByteArray())

        @Nested
        @DisplayName("and the start index equals the input end index")
        inner class StartEQEnd {

          @Test
          @DisplayName("returns an empty deque")
          fun t1() {
            assertTrue(tgt.slice(4, 4).isEmpty)
          }
        }

        @Nested
        @DisplayName("and the start index is 1 less than the end index")
        inner class Start1LTEnd {

          @Test
          @DisplayName("returns a deque with a single element")
          fun t1() {
            val res = tgt.slice(4, 5)
            assertEquals(1, res.size)
            assertEquals('o'.code.toByte(), res.first)
          }
        }

        @Nested
        @DisplayName("and the start index equals zero while the end index equals the deque's size")
        inner class SameSize {

          @Test
          @DisplayName("returns a copy of the original deque")
          fun t1() {
            val res = tgt.slice(0)
            assertEquals(tgt.size, res.size)
            assertContentEquals(tgt.toArray(), res.toArray())
          }
        }

        @Nested
        @DisplayName("and the start and end indices specify a valid subset of the deque")
        inner class Subset {

          @Test
          @DisplayName("returns a deque wrapping the subset")
          fun t1() {
            val res = tgt.slice(1, 5)
            assertEquals(4, res.size)
            assertEquals("ello", res.toArray().decodeToString())
          }
        }
      }

      @Nested
      @DisplayName("the deque is not inline")
      inner class NotInline {

        val tgt: ByteDeque
          get() {
            val out = ByteDeque(11)
            out.pushTail(" world".toByteArray())
            out.pushHead('o'.code.toByte())
            out.pushHead('l'.code.toByte())
            out.pushHead('l'.code.toByte())
            out.pushHead('e'.code.toByte())
            out.pushHead('h'.code.toByte())
            return out
          }

        @Nested
        @DisplayName("and the start index equals the input end index")
        inner class StartEQEnd {

          @Test
          @DisplayName("returns an empty deque")
          fun t1() {
            assertTrue(tgt.slice(4, 4).isEmpty)
          }
        }

        @Nested
        @DisplayName("and the start index is 1 less than the end index")
        inner class Start1LTEnd {

          @Test
          @DisplayName("returns a deque with a single element")
          fun t1() {
            val res = tgt.slice(4, 5)
            assertEquals(1, res.size)
            assertEquals('o'.code.toByte(), res.first)
          }
        }

        @Nested
        @DisplayName("and the start index equals zero while the end index equals the deque's size")
        inner class SameSize {

          @Test
          @DisplayName("returns a copy of the original deque")
          fun t1() {
            val res = tgt.slice(0)
            assertEquals(tgt.size, res.size)
            assertContentEquals(tgt.toArray(), res.toArray())
          }
        }

        @Nested
        @DisplayName("and the start and end indices specify a valid subset of the deque")
        inner class Subset {

          @Test
          @DisplayName("returns a deque wrapping the subset")
          fun t1() {
            val res = tgt.slice(3, 8)
            assertEquals(5, res.size)
            assertEquals("lo wo", res.toArray().decodeToString())
          }
        }
      }
    }
  }

  @Nested
  @DisplayName("fillFrom(InputStream)")
  inner class FillFrom1 {

    @Nested
    @DisplayName("when the ByteDeque is empty")
    inner class IsEmpty {

    }

    @Nested
    @DisplayName("when the ByteDeque is not empty")
    inner class IsNotEmpty {

      @Nested
      @DisplayName("and is wonky")
      inner class Wonky {

        @Test
        @DisplayName("fills correctly")
        fun t1() {
          val tgt = createWonky(11, "hello ".toByteArray())
          val inp = ByteArrayInputStream(" world!".toByteArray()) // the '!' character wont fit

          tgt.removeTail()
          tgt.fillFrom(inp)

          assertEquals(11, tgt.size)
          assertEquals("hello world", tgt.toArray().decodeToString())
        }
      }
    }
  }

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
  @DisplayName("popInt(Boolean)")
  inner class PopInt1 {

    @Nested
    @DisplayName("when littleEndian is true")
    inner class LittleEndian {

      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, 4))
        assertEquals(67305985, t.popInt(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, -128))
        assertEquals(-2147286527, t.popInt(true))
        assertTrue(t.isEmpty)
      }
    }

    @Nested
    @DisplayName("when littleEndian is false")
    inner class BigEndian {
      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, 4))
        assertEquals(16909060, t.popInt())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(-128, 3, 2, 1))
        assertEquals(-2147286527, t.popInt())
        assertTrue(t.isEmpty)
      }
    }
  }

  @Nested
  @DisplayName("popLong(Boolean)")
  inner class PopLong1 {

    @Nested
    @DisplayName("when littleEndian is true")
    inner class LittleEndian {

      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(578437695752307201, t.popLong(true))
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, 4, 5, 6, 7, -128))
        assertEquals(-9221395093405892095, t.popLong(true))
        assertTrue(t.isEmpty)
      }
    }

    @Nested
    @DisplayName("when littleEndian is false")
    inner class BigEndian {
      @Test
      @DisplayName("correctly parses the bytes for a positive short value")
      fun t1() {
        val t = ByteDeque(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(72623859790382856, t.popLong())
        assertTrue(t.isEmpty)
      }

      @Test
      @DisplayName("correctly parses the bytes for a negative short value")
      fun t2() {
        val t = ByteDeque(byteArrayOf(-128, 7, 6, 5, 4, 3, 2, 1))
        assertEquals(-9221395093405892095, t.popLong())
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

}