package io.foxcapades.lib.pdk

/**
 * # Double Deque
 *
 * A deque type that deals in unboxed Double values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since v1.0.0
 */
@Suppress("NOTHING_TO_INLINE")
class DoubleDeque : PrimitiveDeque<Double, DoubleArray> {

  private var container: DoubleArray

  override var size = 0
    private set

  override val cap
    get() = container.size

  override val space: Int
    get() = container.size - size

  /**
   * Indicates whether the data in this deque is currently inline
   */
  private inline val isInline: Boolean
    get() = realHead + size <= container.size

  // region Constructors


  /**
   * Constructs a new, empty [DoubleDeque] instance.
   *
   * @param capacity Initial capacity to create this deque with.
   */
  constructor(capacity: Int = 0) {
    this.container = DoubleArray(capacity)
  }

  /**
   * Constructs a new [DoubleDeque] instance copying the given values.
   *
   * @param data Array of values to copy into the new deque.
   */
  constructor(data: DoubleArray) {
    this.container = data.copyOf()
    this.size = data.size
  }

  /**
   * Constructs a new [DoubleDeque] instance wrapping the given values.
   *
   * @param data Raw array that will back the new instance.
   *
   * @param head Internal head position for the new instance.
   */
  private constructor(data: DoubleArray, head: Int) {
    this.container     = data
    this.size     = data.size
    this.realHead = head
  }

  // endregion Constructors

  // region Front
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods specifically relating to or operating on the head end of the
  //  deque.
  //

  // region Get Head

  /**
   * The first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  val head get() = if (isEmpty) throw NoSuchElementException() else container[realHead]

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


  // endregion Get Head

  // region Pop

  /**
   * Removes the first element from this deque and returns it.
   *
   * @return The former first element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  fun pop(): Double {
    if (isEmpty)
      throw NoSuchElementException()

    val c = container[realHead]

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
  fun push(value: Double) {
    ensureCapacity(size + 1)
    realHead = decremented(realHead)
    container[realHead] = value
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
  inline fun pushHead(value: Double) = push(value)

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
  inline fun pushFirst(value: Double) = push(value)

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
  inline fun pushFront(value: Double) = push(value)

  //////////////////////////////////////////////////////////////////////////////
  // endregion Push

  //////////////////////////////////////////////////////////////////////////////
  // endregion Front

  // region Back
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods specifically relating to or operating on the tail end of the
  //  deque.
  //

  // region Get Tail

  /**
   * The last element in this deque.
   *
   * @throws NoSuchElementException if this deque is empty.
   */
  val tail get() = if (isEmpty) throw NoSuchElementException() else container[internalIndex(lastIndex)]

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

  // endregion Get Tail

  // region Pop

  /**
   * Removes the last element from this deque and returns it.
   *
   * @return The former last element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  fun popTail(): Double {
    if (isEmpty)
      throw NoSuchElementException()

    val c = container[internalIndex(lastIndex)]
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
  inline fun popLast(): Double = popTail()

  /**
   * Removes the last element from this deque and returns it.
   *
   * Alias of [popTail]
   *
   * @return The former last element in this deque.
   *
   * @throws NoSuchElementException If this deque is empty.
   */
  inline fun popBack(): Double = popTail()


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

  //////////////////////////////////////////////////////////////////////////////
  // endregion Remove

  // region Push
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods for pushing elements onto the tail end of the deque
  //

  // region Push Single Value
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods for pushing elements onto the tail end of the deque one element at
  //  a time.
  //

  /**
   * Pushes the given value onto the back of this deque.
   *
   * If the capacity of this deque was equal to its size at the time of this
   * method call, the internal container will be resized to accommodate the new
   * value.
   *
   * @param value Value that will be pushed onto the back of this deque.
   */
  fun pushTail(value: Double) {
    ensureCapacity(size + 1)
    container[internalIndex(size)] = value
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
  inline fun pushLast(value: Double) = pushTail(value)

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
  inline fun pushBack(value: Double) = pushTail(value)

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
  inline operator fun plusAssign(value: Double) = pushTail(value)

  // endregion Push Single Value

  // region Push Multiple Values
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods for pushing elements onto the tail end of the deque en masse
  //

  override fun pushTail(values: DoubleArray) {
    // If the input array is empty, then we have nothing to do.
    if (values.isEmpty())
      return

    // If this deque is empty, we can just copy the input array as our new
    // backing buffer.
    if (container.isEmpty()) {
      container = values.copyOf()
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
      values.copyInto(container, oldTail)
    } else {

      // So the new tail is before the old tail in the buffer array, we need to do
      // 2 array copies: one from the current tail till the end of the buffer
      // array, the next from the start of the buffer array until the new tail
      // position

      // Copy from the old tail until the end of the data buffer
      values.copyInto(container, oldTail, 0, container.size - oldTail)
      // Copy from the start of the data buffer
      values.copyInto(container, 0, container.size - oldTail)
    }

    size += values.size
  }

  override fun pushTail(values: Collection<Double>) {
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
  fun pushTail(values: DoubleDeque) {
    // If the input deque is empty, then we have nothing to do.
    if (values.isEmpty)
      return

    // If our backing buffer is empty, then we can just clone the given deque
    if (container.isEmpty()) {
      container = values.container.copyOf()
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
        values.container.copyInto(container, oldTail, values.realHead, values.realHead + values.size)
      }

      // If we aren't currently inline, then we have to do 2 array copies, one
      // to the 'front' segment at the back of our data array, and one to the
      // 'back' segment at the front of our data array.
      else {
        val chunk1Size = container.size - oldTail
        values.container.copyInto(container, oldTail, 0, chunk1Size)
        values.container.copyInto(container, 0, values.size - chunk1Size, values.size)
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
  inline fun pushLast(values: DoubleDeque) = pushTail(values)

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
  inline fun pushBack(values: DoubleDeque) = pushTail(values)

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
  inline operator fun plusAssign(values: DoubleDeque) = pushTail(values)

  //////////////////////////////////////////////////////////////////////////////
  // endregion Push Multiple Values

  //////////////////////////////////////////////////////////////////////////////
  // endregion Push

  //////////////////////////////////////////////////////////////////////////////
  // endregion Back

  // region Positionless
  //////////////////////////////////////////////////////////////////////////////
  //
  //  Methods that are not particularly related to either end of the deque.
  //

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
  operator fun set(index: Int, value: Double) = container.set(internalIndex(validExtInd(index)), value)

  /**
   * Gets the value at the given index from this deque.
   *
   * @param index Index of the value that should be retrieved.
   *
   * @throws IndexOutOfBoundsException If the given index is less than zero or
   * is greater than [lastIndex].
   */
  operator fun get(index: Int) = container[internalIndex(validExtInd(index))]

  /**
   * Tests whether this deque contains the given value.
   */
  operator fun contains(value: Double): Boolean {
    for (v in container)
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
  operator fun plus(rhs: DoubleDeque): DoubleDeque {
    val buf = DoubleArray(size + rhs.size)

    copyInto(buf)
    rhs.copyInto(buf, size)

    return DoubleDeque(buf, 0)
  }

  override fun clear() {
    realHead = 0
    size = 0
  }

  override fun copy() = DoubleDeque(container.copyOf(), realHead)

  override fun ensureCapacity(minCapacity: Int) {
    when {
      // If they gave us an invalid capacity
      minCapacity < 0          -> throw IllegalArgumentException()
      // If we already have the desired capacity
      minCapacity <= container.size -> {}
      // If we previously had a capacity of 0
      container.isEmpty()           -> container = DoubleArray(minCapacity)
      // If we need to resize
      else                          -> copyElements(newCap(container.size, minCapacity))
    }
  }

  override fun iterator(): Iterator<Double> {
    return object : Iterator<Double> {
      var pos = 0

      override fun hasNext(): Boolean {
        return pos < lastIndex
      }

      override fun next(): Double {
        return get(pos++)
      }
    }
  }

  override fun toArray(): DoubleArray {
    val realTail = internalIndex(lastIndex)

    // If the contents of the deque are in a straight line, we can just copy
    // them out
    if (realHead <= realTail) {
      return container.copyOfRange(realHead, realTail + 1)
    }

    val out = DoubleArray(size)

    // Copy the front of the output array out of the back portion of our data
    container.copyInto(out, 0, realHead, container.size)

    // Copy the back of the output array out of the front portion of our data
    container.copyInto(out, container.size - realHead, 0, realTail + 1)

    return out
  }

  override fun toList() = toArray().asList()

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
  fun copyInto(array: DoubleArray, offset: Int = 0) {
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
      container.copyInto(array, offset, realHead, realTail + 1)
      return
    }

    // Number of values we have starting from the head of the deque that are on
    // the back end of the data array.
    val leaders = container.size - realHead

    // Number of values we have starting from the 'middle' of the deque that are
    // on the front end of the data array.
    val trailers = realTail + 1

    // Copy the front of the deque from the back of our array to the front of
    // theirs.
    container.copyInto(array, offset, realHead, realHead + leaders)

    // Copy at most [trailers] values into their array.  If their array is not
    // long enough to hold [trailers] values, then [remainder] values will be
    // copied in instead.
    container.copyInto(array, offset + leaders, 0, trailers)
  }

  override fun slice(start: Int, end: Int) = DoubleDeque(sliceToArray(start, end), 0)

  override fun slice(range: IntRange) = DoubleDeque(sliceToArray(range.first, range.last+1), 0)

  override fun sliceToArray(start: Int, end: Int): DoubleArray {
    // If they gave us one or more invalid indices, throw an exception
    if (start !in 0 until size || start > end || end > size)
      throw IndexOutOfBoundsException()

    val realSize  = end - start

    // Shortcuts
    when (realSize) {
      0    -> return DoubleArray(0)
      1    -> return DoubleArray(1) { container[internalIndex(start)] }
      size -> return toArray()
    }

    val realStart = internalIndex(start)
    val realEnd   = internalIndex(end)

    val out = DoubleArray(realSize)

    // If the values are inline, we can just arraycopy out
    if (realStart < realEnd) {
      container.copyInto(out, 0, realStart, realEnd)
    }

    // The values are out of line, we have to do 2 copies
    else {
      container.copyInto(out, 0, realStart, container.size)
      container.copyInto(out, container.size - realStart, 0, realEnd)
    }

    return out
  }

  override fun sliceToArray(range: IntRange) = sliceToArray(range.first, range.last+1)

  override fun compact() = copyElements(cap)

  override fun trimToSize() = copyElements(size)

  override fun toString() = "DoubleDeque($size:$cap)"

  override fun equals(other: Any?) = if (other is DoubleDeque) container.contentEquals(other.container) else false

  override fun hashCode() = container.contentHashCode()

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
    val new = DoubleArray(newCap)
    container.copyInto(new, 0, realHead, container.size)
    container.copyInto(new, container.size - realHead, 0, realHead)
    realHead = 0
    container = new
  }

  companion object {

    /**
     * Creates a new [DoubleDeque] instance wrapping the given values.
     *
     * @param values Values to wrap.
     *
     * @return A new deque wrapping the given values.
     */
    @JvmStatic
    fun of(vararg values: Double) = DoubleDeque(values, 0)
  }
}