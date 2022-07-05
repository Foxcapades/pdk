package io.foxcapades.lib.pdk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ByteQueue")
internal class ByteDequeTest {

  @Nested
  @DisplayName("size")
  inner class Size {

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
    @DisplayName("when the deque needst to resize to the required capacity")
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
}