package io.foxcapades.lib.pdk

import java.io.InputStream
import java.nio.BufferUnderflowException

/**
 * # Byte Deque
 *
 * A deque type that deals in unboxed Byte values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since v1.0.0
 */
@Suppress("NOTHING_TO_INLINE")
class ByteDeque : PrimitiveDeque<Byte, ByteArray> {

  companion object {

    @JvmStatic
    fun of(vararg values: Byte) = ByteDeque(values, 0)
  }

  // region Public API
  // ###################################################################################################################

  // region Positionless
  // ###########################################################################

  override var size = 0
    private set

  override val cap
    get() = data.size

  override val space: Int
    get() = data.size - size



  /**
   * Constructs a new, empty [ByteDeque] instance.
   *
   * @param capacity Initial capacity to create this deque with.
   */
  constructor(capacity: Int = 0) {
    this.data = ByteArray(capacity)
  }

  /**
   * Constructs a new [ByteDeque] instance copying the given values.
   *
   * @param data Array of values to copy into the new deque.
   */
  constructor(data: ByteArray) {
    this.data = data.copyOf()
    this.size = data.size
  }

  /**
   * Constructs a new [ByteDeque] instance wrapping the given values.
   *
   * @param data Raw array that will back the new instance.
   *
   * @param head Internal head position for the new instance.
   */
  private constructor(data: ByteArray, head: Int) {
    this.data     = data
    this.size     = data.size
    this.realHead = head
  }



  /**
   * Copies data from this deque into the given array.
   *
   * If either this deque, or the given array are empty, nothing is copied.
   *
   * If this deque's size is greater than the length of the given array, only
   * those values that can fit into the given array will be copied.
   *
   * If the given array's size is greater than the size of this deque, at most
   * [size] values will be copied into the given array.
   *
   * @param array Array into which values should be copied from this deque.
   *
   * @param offset Offset in the input array at which values should start to be
   * copied.
   */
  fun copyInto(array: ByteArray, offset: Int = 0) {
    // If the input array is empty, return because we can't put anything into
    // an empty array.
    //
    // If this deque is empty, return because we have nothing to put into the
    // given array.
    //
    // If the offset is greater than or equal to the given array size, then
    // there is no room into which we can copy anything.
    if (array.isEmpty() || this.isEmpty || offset >= array.size)
      return

    // How much room we actually have to work with in the input array.
    val rem = array.size - offset

    // Figure out the actual position of the last desired element.
    //
    // If the amount of room we have to fill in the given array is larger than
    // the number of values we actually have, then the last value will be
    // tail of this deque.
    //
    // If the amount of room we have to fill in the given array is smaller than
    // the number of values we actually have, then the last value will be the
    // value at position `rem - 1`.
    val realTail = if (rem > size)
      internalIndex(lastIndex)
    else
      internalIndex(rem - 1)

    // If the desired data is in a straight line (unbroken)
    if (realHead <= realTail) {
      // then we can straight copy and be done
      data.copyInto(array, offset, realHead, realTail + 1)
      return
    }

    // Number of values we have starting from the head of the deque that are on
    // the back end of the data array.
    val leaders = data.size - realHead

    // Number of values we have starting from the 'middle' of the deque that are
    // on the front end of the data array.
    val trailers = realTail + 1

    // Copy the front of the deque from the back of our array to the front of
    // theirs.
    data.copyInto(array, offset, realHead, realHead + leaders)

    // Copy at most [trailers] values into their array.  If their array is not
    // long enough to hold [trailers] values, then [remainder] values will be
    // copied in instead.
    data.copyInto(array, offset + leaders, 0, trailers)
  }

  override fun slice(start: Int, end: Int) = ByteDeque(sliceToArray(start, end), 0)

  override fun slice(range: IntRange) = ByteDeque(sliceToArray(range.first, range.last+1), 0)

  override fun sliceToArray(start: Int, end: Int): ByteArray {
    // If they gave us one or more invalid indices, throw an exception
    if (start !in 0 until size || start > end || end > size)
      throw IndexOutOfBoundsException()

    val realSize  = end - start

    // Shortcuts
    when (realSize) {
      0    -> return ByteArray(0)
      1    -> return ByteArray(1) { data[internalIndex(start)] }
      size -> return toArray()
    }

    val realStart = internalIndex(start)
    val realEnd   = internalIndex(end)

    val out = ByteArray(realSize)

    // If the values are inline, we can just arraycopy out
    if (realStart < realEnd) {
      data.copyInto(out, 0, realStart, realEnd)
    }

    // The values are out of line, we have to do 2 copies
    else {
      data.copyInto(out, 0, realStart, data.size)
      data.copyInto(out, data.size - realStart, 0, realEnd)
    }

    return out
  }

  override fun sliceToArray(range: IntRange) = sliceToArray(range.first, range.last+1)

  override fun copy() = ByteDeque(data.copyOf(), realHead)

  override fun toArray(): ByteArray {
    val realTail = internalIndex(lastIndex)

    // If the contents of the deque are in a straight line, we can just copy
    // them out
    if (realHead <= realTail) {
      return data.copyOfRange(realHead, realTail + 1)
    }

    val out = ByteArray(size)

    // Copy the front of the output array out of the back portion of our data
    data.copyInto(out, 0, realHead, data.size)

    // Copy the back of the output array out of the front portion of our data
    data.copyInto(out, data.size - realHead, 0, realTail + 1)

    return out
  }

  override fun toList() = toArray().asList()



  /**
   * Sets the value at the given index in this deque.
   *
   * @param index Index at which the value should be set/overwritten.
   *
   * @param value Value to set.
   *
   * @throws IndexOutOfBoundsException If the given index is less than zero or
   * is greater than [lastIndex].
   */
  operator fun set(index: Int, value: Byte) = data.set(internalIndex(validExtInd(index)), value)

  /**
   * Gets the value at the given index from this deque.
   *
   * @param index Index of the value that should be retrieved.
   *
   * @throws IndexOutOfBoundsException If the given index is less than zero or
   * is greater than [lastIndex].
   */
  operator fun get(index: Int) = data[internalIndex(validExtInd(index))]

  /**
   * Tests whether this deque contains the given value.
   */
  operator fun contains(value: Byte): Boolean {
    for (v in data)
      if (v == value)
        return true

    return false
  }

  /**
   * Combines the content of this deque with the given other deque to create a
   * new deque instance with the concatenated content of both original deques.
   *
   * Does not modify the state of either input deque.
   *
   * @param rhs Deque whose contents will be concatenated with the contents of
   * this deque to create a new deque instance.
   *
   * @return A new deque instance containing the concatenated contents of this
   * deque and the given input deque.
   */
  operator fun plus(rhs: ByteDeque): ByteDeque {
    val buf = ByteArray(size + rhs.size)

    copyInto(buf)
    rhs.copyInto(buf, size)

    return ByteDeque(buf, 0)
  }



  override fun clear() {
    realHead = 0
    size = 0
  }

  override fun ensureCapacity(minCapacity: Int) {
    when {
      // If they gave us an invalid capacity
      minCapacity < 0          -> throw IllegalArgumentException()
      // If we already have the desired capacity
      minCapacity <= data.size -> {}
      // If we previously had a capacity of 0
      data.isEmpty()           -> data = ByteArray(minCapacity)
      // If we need to resize
      else                     -> copyElements(newCap(data.size, minCapacity))
    }
  }

  override fun compact() = copyElements(cap)

  override fun trimToSize() = copyElements(size)



  override fun toString() = "ByteDeque($size:$cap)"

  override fun equals(other: Any?) = if (other is ByteDeque) data.contentEquals(other.data) else false

  override fun hashCode() = data.contentHashCode()



  override fun iterator(): Iterator<Byte> {
    return object : Iterator<Byte> {
      var pos = 0

      override fun hasNext(): Boolean {
        return pos < lastIndex
      }

      override fun next(): Byte {
        return get(pos++)
      }
    }
  }


  // ###########################################################################
  // endregion Positionless

  // region Head
  // ###########################################################################

  // region Access

  /**
   * The first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  val head get() = if (isEmpty) throw NoSuchElementException() else data[realHead]

  /**
   * The first element in this deque.
   *
   * Alias of [head].
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline val first get() = head

  /**
   * The first element in this deque.
   *
   * Alias of [head].
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline val front get() = head

  /**
   * Returns the first element in this deque.
   *
   * Alias of [head].
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun head() = head

  /**
   * Returns the first element in this deque.
   *
   * Alias of [head].
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun first() = head

  /**
   * Returns the first element in this deque.
   *
   * Alias of [head].
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun front() = head

  // endregion Access

  // region Pop

  /**
   * Removes the first element from this deque and returns it.
   *
   * @return The former first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  fun pop(): Byte {
    if (isEmpty)
      throw NoSuchElementException()

    val c = data[realHead]

    realHead = incremented(realHead)
    size--

    return c
  }

  /**
   * Removes the first element from this deque and returns it.
   *
   * Alias of [pop]
   *
   * @return The former first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popHead() = pop()

  /**
   * Removes the first element from this deque and returns it.
   *
   * Alias of [pop]
   *
   * @return The former first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popFirst() = pop()

  /**
   * Removes the first element from this deque and returns it.
   *
   * Alias of [pop]
   *
   * @return The former first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popFront() = pop()

  /**
   * Pops the first 2 bytes from this [ByteDeque] and translates them into a
   * [Short] value.
   *
   * If this deque contains fewer than 2 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * @return The parsed `Short` value.
   *
   * Defaults to `false` (big endian).
   *
   * @throws BufferUnderflowException If this deque contains fewer than 2 bytes
   * when this method is called.
   */
  fun popShort(littleEndian: Boolean = false): Short {
    if (size < 2)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(1)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 2
      size -= 2
      return if (littleEndian)
        ((data[head].toInt()   and 0xFF)         or
        ((data[head+1].toInt() and 0xFF) shl 8)).toShort()
      else
        (((data[head].toInt() and 0xFF) shl 8)  or
        (data[head+1].toInt()  and 0xFF)).toShort()
    }

    size -= 2

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      ((data[internalIndex(0)].toInt()  and 0xFF) or
      ((data[internalIndex(1)].toInt() and 0xFF) shl 8)).toShort()
    else
      (((data[internalIndex(0)].toInt() and 0xFF) shl 8) or
      (data[internalIndex(1)].toInt()  and 0xFF)).toShort()

    realHead = internalIndex(2)

    return out
  }

  /**
   * Pops the first 4 bytes from this [ByteDeque] and translates them into an
   * [Int] value.
   *
   * If this deque contains fewer than 4 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The parsed `Int` value.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 4 bytes
   * when this method is called.
   */
  fun popInt(littleEndian: Boolean = false): Int {
    if (size < 4)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(3)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 4
      size -= 4
      return if (littleEndian)
        (data[head].toInt()    and 0xFF)         or
        ((data[head+1].toInt() and 0xFF) shl 8)  or
        ((data[head+2].toInt() and 0xFF) shl 16) or
        ((data[head+3].toInt() and 0xFF) shl 24)
      else
        ((data[head].toInt() and 0xFF)   shl 24) or
        ((data[head+1].toInt() and 0xFF) shl 16) or
        ((data[head+2].toInt() and 0xFF) shl 8)  or
        (data[head+3].toInt()  and 0xFF)
    }

    size -= 4

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      (data[internalIndex(0)].toInt()  and 0xFF)         or
      ((data[internalIndex(1)].toInt() and 0xFF) shl 8)  or
      ((data[internalIndex(2)].toInt() and 0xFF) shl 16) or
      ((data[internalIndex(3)].toInt() and 0xFF) shl 24)
    else
      ((data[internalIndex(0)].toInt() and 0xFF) shl 24) or
      ((data[internalIndex(1)].toInt() and 0xFF) shl 16) or
      ((data[internalIndex(2)].toInt() and 0xFF) shl 8)  or
      (data[internalIndex(3)].toInt()  and 0xFF)

    realHead = internalIndex(4)

    return out
  }

  /**
   * Pops the first 8 bytes from this [ByteDeque] and translates them into a
   * [Long] value.
   *
   * If this deque contains fewer than 8 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The parsed `Long` value.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 8 bytes
   * when this method is called.
   */
  fun popLong(littleEndian: Boolean = false): Long {
    if (size < 8)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(7)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 8
      size -= 8
      return if (littleEndian)
        (data[head].toLong()  and 0xFFL)         or
        ((data[head+1].toLong() and 0xFFL) shl 8)  or
        ((data[head+2].toLong() and 0xFFL) shl 16) or
        ((data[head+3].toLong() and 0xFFL) shl 24) or
        ((data[head+4].toLong() and 0xFFL) shl 32) or
        ((data[head+5].toLong() and 0xFFL) shl 40) or
        ((data[head+6].toLong() and 0xFFL) shl 48) or
        ((data[head+7].toLong() and 0xFFL) shl 56)
      else
        ((data[head].toLong() and 0xFFL) shl 56) or
        ((data[head+1].toLong() and 0xFFL) shl 48) or
        ((data[head+2].toLong() and 0xFFL) shl 40) or
        ((data[head+3].toLong() and 0xFFL) shl 32) or
        ((data[head+4].toLong() and 0xFFL) shl 24) or
        ((data[head+5].toLong() and 0xFFL) shl 16) or
        ((data[head+6].toLong() and 0xFFL) shl 8)  or
        (data[head+7].toLong()  and 0xFFL)
    }

    size -= 8

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      (data[internalIndex(0)].toLong()  and 0xFFL)         or
      ((data[internalIndex(1)].toLong() and 0xFFL) shl 8)  or
      ((data[internalIndex(2)].toLong() and 0xFFL) shl 16) or
      ((data[internalIndex(3)].toLong() and 0xFFL) shl 24) or
      ((data[internalIndex(4)].toLong() and 0xFFL) shl 32) or
      ((data[internalIndex(5)].toLong() and 0xFFL) shl 40) or
      ((data[internalIndex(6)].toLong() and 0xFFL) shl 48) or
      ((data[internalIndex(7)].toLong() and 0xFFL) shl 56)
    else
      ((data[internalIndex(0)].toLong() and 0xFFL) shl 56) or
      ((data[internalIndex(1)].toLong() and 0xFFL) shl 48) or
      ((data[internalIndex(2)].toLong() and 0xFFL) shl 40) or
      ((data[internalIndex(3)].toLong() and 0xFFL) shl 32) or
      ((data[internalIndex(4)].toLong() and 0xFFL) shl 24) or
      ((data[internalIndex(5)].toLong() and 0xFFL) shl 16) or
      ((data[internalIndex(6)].toLong() and 0xFFL) shl 8)  or
      (data[internalIndex(7)].toLong()  and 0xFFL)

    realHead = internalIndex(8)

    return out
  }

  /**
   * Takes the first byte from the backing [ByteDeque] and translates it into a
   * [UByte] value.
   *
   * If this deque is empty, this method throws a [BufferUnderflowException].
   *
   * @return The parsed `UByte` value.
   *
   * @throws BufferUnderflowException If this deque is empty when this method is
   * called.
   */
  fun popUByte(): UByte = pop().toUByte()

  /**
   * Pops the first 2 bytes from this [ByteDeque] and translates them into a
   * [UShort] value.
   *
   * If this deque contains fewer than 2 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The parsed `UShort` value.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 2 bytes
   * when this method is called.
   */
  fun popUShort(littleEndian: Boolean = false): UShort {
    if (size < 2)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(1)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 2
      size -= 2
      return if (littleEndian)
        ((data[head].toInt()   and 0xFF) or
        ((data[head+1].toInt() and 0xFF) shl 8)).toUShort()
      else
        (((data[head].toInt() and 0xFF) shl 8)  or
        (data[head+1].toInt() and 0xFF)).toUShort()
    }

    size -= 2

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      ((data[internalIndex(0)].toInt() and 0xFF) or
      ((data[internalIndex(1)].toInt() and 0xFF) shl 8)).toUShort()
    else
      (((data[internalIndex(0)].toInt() and 0xFF) shl 8) or
      (data[internalIndex(1)].toInt()   and 0xFF)).toUShort()

    realHead = internalIndex(2)

    return out
  }

  /**
   * Pops the first 4 bytes from this [ByteDeque] and translates them into a
   * [UInt] value.
   *
   * If this deque contains fewer than 4 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The parsed `UInt` value.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 4 bytes
   * when this method is called.
   */
  fun popUInt(littleEndian: Boolean = false): UInt {
    if (size < 4)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(3)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 4
      size -= 4
      return if (littleEndian)
        (data[head].toUInt()  and 0xFFu)           or
        ((data[head+1].toUInt() and 0xFFu) shl 8)  or
        ((data[head+2].toUInt() and 0xFFu) shl 16) or
        ((data[head+3].toUInt() and 0xFFu) shl 24)
      else
        ((data[head].toUInt() and 0xFFu) shl 24)   or
        ((data[head+1].toUInt() and 0xFFu) shl 16) or
        ((data[head+2].toUInt() and 0xFFu) shl 8)  or
        (data[head+3].toUInt()  and 0xFFu)
    }

    size -= 4

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      (data[internalIndex(0)].toUInt()  and 0xFFu)         or
      ((data[internalIndex(1)].toUInt() and 0xFFu) shl 8)  or
      ((data[internalIndex(2)].toUInt() and 0xFFu) shl 16) or
      ((data[internalIndex(3)].toUInt() and 0xFFu) shl 24)
    else
      ((data[internalIndex(0)].toUInt() and 0xFFu) shl 24) or
      ((data[internalIndex(1)].toUInt() and 0xFFu) shl 16) or
      ((data[internalIndex(2)].toUInt() and 0xFFu) shl 8)  or
      (data[internalIndex(3)].toUInt()  and 0xFFu)

    realHead = internalIndex(4)

    return out
  }

  /**
   * Pops the first 8 bytes from this [ByteDeque] and translates them into a
   * [ULong] value.
   *
   * If this deque contains fewer than 8 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @throws BufferUnderflowException If this deque contains fewer than 8 bytes
   * when this method is called.
   */
  fun popULong(littleEndian: Boolean = false): ULong {
    if (size < 8)
      throw BufferUnderflowException()

    val head      = realHead
    val lastIndex = internalIndex(7)

    // Our data is inline
    if (head < lastIndex) {
      realHead = head + 8
      size -= 8
      return if (littleEndian)
        (data[head].toULong()  and 0xFFu)         or
        ((data[head+1].toULong() and 0xFFu) shl 8)  or
        ((data[head+2].toULong() and 0xFFu) shl 16) or
        ((data[head+3].toULong() and 0xFFu) shl 24) or
        ((data[head+4].toULong() and 0xFFu) shl 32) or
        ((data[head+5].toULong() and 0xFFu) shl 40) or
        ((data[head+6].toULong() and 0xFFu) shl 48) or
        ((data[head+7].toULong() and 0xFFu) shl 56)
      else
        ((data[head].toULong() and 0xFFu) shl 56) or
        ((data[head+1].toULong() and 0xFFu) shl 48) or
        ((data[head+2].toULong() and 0xFFu) shl 40) or
        ((data[head+3].toULong() and 0xFFu) shl 32) or
        ((data[head+4].toULong() and 0xFFu) shl 24) or
        ((data[head+5].toULong() and 0xFFu) shl 16) or
        ((data[head+6].toULong() and 0xFFu) shl 8)  or
        (data[head+7].toULong()  and 0xFFu)
    }

    size -= 8

    // We are out of line, so just use the internal indices to get the values
    // rather than doing a bunch of complex logic to make it happen.
    val out = if (littleEndian)
      (data[internalIndex(0)].toULong()  and 0xFFu)         or
      ((data[internalIndex(1)].toULong() and 0xFFu) shl 8)  or
      ((data[internalIndex(2)].toULong() and 0xFFu) shl 16) or
      ((data[internalIndex(3)].toULong() and 0xFFu) shl 24) or
      ((data[internalIndex(4)].toULong() and 0xFFu) shl 32) or
      ((data[internalIndex(5)].toULong() and 0xFFu) shl 40) or
      ((data[internalIndex(6)].toULong() and 0xFFu) shl 48) or
      ((data[internalIndex(7)].toULong() and 0xFFu) shl 56)
    else
      ((data[internalIndex(0)].toULong() and 0xFFu) shl 56) or
      ((data[internalIndex(1)].toULong() and 0xFFu) shl 48) or
      ((data[internalIndex(2)].toULong() and 0xFFu) shl 40) or
      ((data[internalIndex(3)].toULong() and 0xFFu) shl 32) or
      ((data[internalIndex(4)].toULong() and 0xFFu) shl 24) or
      ((data[internalIndex(5)].toULong() and 0xFFu) shl 16) or
      ((data[internalIndex(6)].toULong() and 0xFFu) shl 8)  or
      (data[internalIndex(7)].toULong()  and 0xFFu)

    realHead = internalIndex(8)

    return out
  }

  /**
   * Pops the first 4 bytes from this [ByteDeque] and translates them into a
   * [Float] value.
   *
   * If this deque contains fewer than 4 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The `Float` value parsed from the first 4 bytes popped from this
   * deque.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 4 bytes
   * when this method is called.
   */
  fun popFloat(littleEndian: Boolean = false): Float =
    Float.fromBits(popInt(littleEndian))

  /**
   * Pops the first 8 bytes from this [ByteDeque] and translates them into a
   * [Double] value.
   *
   * If this deque contains fewer than 8 bytes, this method throws a
   * [BufferUnderflowException].
   *
   * @param littleEndian Boolean flag indicating whether the bytes in the deque
   * should be translated to an int with a little endian byte order.
   *
   * Defaults to `false` (big endian).
   *
   * @return The `Double` value parsed from the first 8 bytes popped from this
   * deque.
   *
   * @throws BufferUnderflowException If this deque contains fewer than 8 bytes
   * when this method is called.
   */
  fun popDouble(littleEndian: Boolean = false): Double =
    Double.fromBits(popLong(littleEndian))

  // endregion Pop

  // region Remove

  override fun removeHead(count: Int) {
    when {
      count < 0     -> throw IllegalArgumentException()
      isEmpty       -> {}
      count == 0    -> {}
      count >= size -> clear()
      else          -> {
        realHead = internalIndex(count)
        size -= count
      }
    }
  }

  // endregion Remove

  // region Push

  /**
   * Pushes the given value onto the front of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * @param value Value that will be pushed onto the front of this deque.
   */
  fun push(value: Byte) {
    ensureCapacity(size + 1)
    realHead = decremented(realHead)
    data[realHead] = value
    size++
  }

  /**
   * Pushes the given value onto the front of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias for [push]
   *
   * @param value Value that will be pushed onto the front of this deque.
   */
  inline fun pushHead(value: Byte) = push(value)

  /**
   * Pushes the given value onto the front of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias for [push]
   *
   * @param value Value that will be pushed onto the front of this deque.
   */
  inline fun pushFirst(value: Byte) = push(value)

  /**
   * Pushes the given value onto the front of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias for [push]
   *
   * @param value Value that will be pushed onto the front of this deque.
   */
  inline fun pushFront(value: Byte) = push(value)

  //////////////////////////////////////////////////////////////////////////////
  // endregion Push

  // ###########################################################################
  // endregion Head

  // region Tail
  // ###########################################################################

  // region Access

  /**
   * The last element in this deque.
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  val tail get() = if (isEmpty) throw NoSuchElementException() else data[internalIndex(lastIndex)]

  /**
   * The last element in this deque.
   *
   * Alias for [tail]
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  inline val last get() = tail

  /**
   * The last element in this deque.
   *
   * Alias for [tail]
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  inline val back get() = tail

  /**
   * Returns the last element in this deque.
   *
   * Alias for [tail]
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  inline fun tail() = tail

  /**
   * Returns the last element in this deque.
   *
   * Alias for [tail]
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  inline fun last() = tail

  /**
   * Returns the last element in this deque.
   *
   * Alias for [tail]
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  inline fun back() = tail

  // endregion Access

  // region Pop

  /**
   * Removes the last element from this deque and returns it.
   *
   * @return The former last element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  fun popTail(): Byte {
    if (isEmpty)
      throw NoSuchElementException()

    val c = data[internalIndex(lastIndex)]
    size--
    return c
  }

  /**
   * Removes the last element from this deque and returns it.
   *
   * Alias of [popTail]
   *
   * @return The former last element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popLast(): Byte = popTail()

  /**
   * Removes the last element from this deque and returns it.
   *
   * Alias of [popTail]
   *
   * @return The former last element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popBack(): Byte = popTail()


  // endregion Pop

  // region Remove

  override fun removeTail(count: Int) {
    when {
      count  < 0    ->  throw IllegalArgumentException()
      count == 0    -> {}
      isEmpty       -> {}
      count >= size -> clear()
      else          -> size -= count
    }
  }

  // endregion Remove

  // region Push

  /**
   * Pushes the given value onto the back of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * @param value Value that will be pushed onto the back of this deque.
   */
  fun pushTail(value: Byte) {
    ensureCapacity(size + 1)
    data[internalIndex(size)] = value
    size++
  }

  /**
   * Pushes the given value onto the back of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias of [pushTail]
   *
   * @param value Value that will be pushed onto the back of this deque.
   */
  inline fun pushLast(value: Byte) = pushTail(value)

  /**
   * Pushes the given value onto the back of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias of [pushTail]
   *
   * @param value Value that will be pushed onto the back of this deque.
   */
  inline fun pushBack(value: Byte) = pushTail(value)

  /**
   * Pushes the given value onto the back of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * Alias of [pushTail]
   *
   * @param value Value that will be pushed onto the back of this deque.
   */
  inline operator fun plusAssign(value: Byte) = pushTail(value)

  override fun pushTail(values: ByteArray) {
    // If the input array is empty, then we have nothing to do.
    if (values.isEmpty())
      return

    // If this deque is empty, we can just copy the input array as our new
    // backing buffer.
    if (data.isEmpty()) {
      data = values.copyOf()
      size = values.size
      return
    }

    // Make sure we have enough room for the contents of the input array.
    ensureCapacity(size + values.size)

    // One past the current last index
    val oldTail = internalIndex(size)
    // New last index
    val newTail = internalIndex(size + values.size)

    // If the new tail is still after the old tail, then we can copy the data in
    // a single array copy
    if (oldTail <= newTail) {
      values.copyInto(data, oldTail)
    } else {

      // So the new tail is before the old tail in the buffer array, we need to do
      // 2 array copies: one from the current tail till the end of the buffer
      // array, the next from the start of the buffer array until the new tail
      // position

      // Copy from the old tail until the end of the data buffer
      values.copyInto(data, oldTail, 0, data.size - oldTail)
      // Copy from the start of the data buffer
      values.copyInto(data, 0, data.size - oldTail)
    }

    size += values.size
  }

  override fun pushTail(values: Collection<Byte>) {
    ensureCapacity(size + values.size)
    for (v in values)
      pushTail(v)
  }

  /**
   * Pushes the contents of the given deque onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input deque, the internal container will be resized to
   * accommodate the new values.
   *
   * @param values Deque that will be pushed onto the back of this deque.
   */
  fun pushTail(values: ByteDeque) {
    // If the input deque is empty, then we have nothing to do.
    if (values.isEmpty)
      return

    // If our backing buffer is empty, then we can just clone the given deque
    if (data.isEmpty()) {
      data = values.data.copyOf()
      size = values.size
      realHead = values.realHead
      return
    }

    // If the other one is just a single value, no need to do all the heavy
    // lifting.
    if (values.size == 1)
      return pushTail(values.head)

    // Make sure we have enough room for everything.
    ensureCapacity(size + values.size)

    // If the incoming data happens to be inline, we can do a simple transfer of
    // one or two array copies.
    if (values.isInline) {

      // Our starting offset, one past the current last value.
      val oldTail = internalIndex(size)
      // Our ending index
      val newTail = internalIndex(size + values.lastIndex)

      // If we are currently inline, then we can just do a single array copy.
      if (oldTail <= newTail) {
        values.data.copyInto(data, oldTail, values.realHead, values.realHead + values.size)
      }

      // If we aren't currently inline, then we have to do 2 array copies, one
      // to the 'front' segment at the back of our data array, and one to the
      // 'back' segment at the front of our data array.
      else {
        val chunk1Size = data.size - oldTail
        values.data.copyInto(data, oldTail, 0, chunk1Size)
        values.data.copyInto(data, 0, values.size - chunk1Size, values.size)
      }

      size += values.size
      return
    }

    // TODO: this should be reserved for the worst case scenario where we could
    //       need 4-8 array copies to do the transfer
    pushTail(values.toArray())
  }

  /**
   * Pushes the contents of the given deque onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input deque, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Deque that will be pushed onto the back of this deque.
   */
  inline fun pushLast(values: ByteDeque) = pushTail(values)

  /**
   * Pushes the contents of the given deque onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input deque, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Deque that will be pushed onto the back of this deque.
   */
  inline fun pushBack(values: ByteDeque) = pushTail(values)

  /**
   * Pushes the contents of the given deque onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input deque, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Deque that will be pushed onto the back of this deque.
   */
  inline operator fun plusAssign(values: ByteDeque) = pushTail(values)

  /**
   * Fills this [ByteDeque] with data from the given [InputStream].
   *
   * This method will read at most `deque.cap - deque.size` bytes from the given
   * `InputStream`.
   *
   * @param stream `InputStream` from which this `ByteDeque` will be filled.
   *
   * @return The number of bytes read into this `ByteDeque` from the given
   * `InputStream`, or `-1` if the end of the `InputStream` had been reached
   * before this method was called.
   */
  fun fillFrom(stream: InputStream): Int {
    // If the current size of the deque is `0` then use the full data array
    // regardless of where the head was previously.
    if (size == 0) {
      realHead = 0
      val red = stream.read(data)

      if (red == -1) {
        size = 0
        return -1
      }

      size = red
      return red
    }

    // If we don't have any space available, then bail here
    if (space == 0)
      return 0

    val oldTail = internalIndex(size)
    val newTail = internalIndex(lastIndex)

    val red = stream.read(data, oldTail, data.size - oldTail)

    if (red == -1)
      return -1

    size += red

    // If we are going to stay inline
    if (oldTail < newTail) {
      return red
    }

    // We are going out of line... they should've compacted :(
    val r2 = stream.read(data, 0, realHead)

    if (r2 == -1)
      return red

    size += r2

    return red + r2
  }

  // endregion Push

  // ###########################################################################
  // endregion Tail

  // ###################################################################################################################
  // endregion Public API

  private var data: ByteArray

  /**
   * Indicates whether the data in this deque is currently inline
   */
  private inline val isInline: Boolean
    get() = realHead + size <= data.size

  // endregion Positionless

  /**
   * Copies the data currently in the backing buffer into a new buffer of size
   * [newCap].
   *
   * The new buffer will be inlined, meaning any 'head' data presently at the
   * tail end of the buffer will be relocated to the beginning of the buffer and
   * any 'tail' data will be put inline after the head data.
   *
   * Example
   * ```
   * newCap   = 8
   * previous = [4, 5, 6, 1, 2, 3]
   * new      = [1, 2, 3, 4, 5, 6, 0, 0]
   * ```
   *
   * This method does not check to see if the resize is necessary ahead of time
   * as it is only called when the necessity of a resize has already been
   * confirmed.
   */
  private fun copyElements(newCap: Int) {
    val new = ByteArray(newCap)
    data.copyInto(new, 0, realHead, data.size)
    data.copyInto(new, data.size - realHead, 0, realHead)
    realHead = 0
    data = new
  }
}
