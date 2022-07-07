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
@OptIn(ExperimentalUnsignedTypes::class)
class DoubleDeque : PrimitiveDeque<Double, DoubleArray> {

  private var data: DoubleArray

  override var size = 0
    private set

  override val cap
    get() = data.size

  override val space: Int
    get() = data.size - size

  /**
   * Indicates whether the data in this deque is currently inline
   */
  private inline val isInline: Boolean
    get() = realHead + size <= data.size

  // region Constructors


  /**
   * Constructs a new, empty [DoubleDeque] instance.
   *
   * @param capacity Initial capacity to create this deque with.
   */
  constructor(capacity: Int = 0) {
    this.data = DoubleArray(capacity)
  }

  /**
   * Constructs a new [DoubleDeque] instance copying the given values.
   *
   * @param data Array of values to copy into the new deque.
   */
  constructor(data: DoubleArray) {
    this.data = data.copyOf()
    this.size = data.size
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


  // endregion Pop

  // region Remove

  /**
   * Removes the first element of this deque.
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  fun removeHead() {
    if (!isEmpty) {
      realHead = incremented(realHead)
      size--
    }
  }

  /**
   * Removes the first element of this deque.
   *
   * Alias of [removeHead]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun removeFirst() = removeHead()

  /**
   * Removes the first element of this deque.
   *
   * Alias of [removeHead]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun removeFront() = removeHead()

  /**
   * Removes the first element of this deque.
   *
   * Alias of [removeHead]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteHead() = removeHead()

  /**
   * Removes the first element of this deque.
   *
   * Alias of [removeHead]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteFirst() = removeHead()

  /**
   * Removes the first element of this deque.
   *
   * Alias of [removeHead]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [pop] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteFront() = removeHead()


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

  /**
   * Removes the last element of this deque.
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  fun removeTail() {
    if (!isEmpty) {
      size--
    }
  }

  /**
   * Removes the last element of this deque.
   *
   * Alias of [removeTail]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun removeLast() = removeTail()

  /**
   * Removes the last element of this deque.
   *
   * Alias of [removeTail]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun removeBack() = removeTail()

  /**
   * Removes the last element of this deque.
   *
   * Alias of [removeTail]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteTail() = removeTail()

  /**
   * Removes the last element of this deque.
   *
   * Alias of [removeTail]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteLast() = removeTail()

  /**
   * Removes the last element of this deque.
   *
   * Alias of [removeTail]
   *
   * If this deque was empty, this method does nothing.
   *
   * This method differs from [popTail] in 2 ways:
   *
   * 1. This method does not return the removed value.
   * 2. This method does not throw an exception if the deque was empty on method
   *    call.
   */
  inline fun deleteBack() = removeTail()


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
  operator fun set(index: Int, value: Double) {
    data[internalIndex(validExtInd(index))] = value
  }

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
  operator fun contains(value: Double): Boolean {
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
  operator fun plus(rhs: DoubleDeque): DoubleDeque {
    val buf = DoubleArray(size + rhs.size)

    copyInto(buf)
    rhs.copyInto(buf, size)

    return DoubleDeque(buf)
  }

  override fun clear() {
    realHead = 0
    size = 0
  }

  override fun copy(): DoubleDeque {
    val nb = DoubleDeque(data)
    nb.realHead = realHead
    return nb
  }

  override fun ensureCapacity(minCapacity: Int) {
    when {
      // If they gave us a invalid capacity
      minCapacity < 0          -> throw IllegalArgumentException()
      // If we already have the desired capacity
      minCapacity <= data.size -> {}
      // If we previously had a capacity of 0
      data.isEmpty()           -> data = DoubleArray(minCapacity)
      // If we need to resize
      else                     -> copyElements(newCap(data.size, minCapacity))
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
      return data.copyOfRange(realHead, realTail + 1)
    }

    val out = DoubleArray(size)

    // Copy the front of the output array out of the back portion of our data
    data.copyInto(out, 0, realHead, data.size)

    // Copy the back of the output array out of the front portion of our data
    data.copyInto(out, data.size - realHead, 0, realTail + 1)

    return out
  }

  override fun toList(): List<Double> {
    return toArray().asList()
  }

  override fun copyInto(array: DoubleArray, offset: Int) {
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

  override fun slice(start: Int, end: Int): DoubleDeque {
    // If they gave us one or more invalid indices, throw an exception
    if (start !in 0 until size || start > end || end > size)
      throw IndexOutOfBoundsException()

    val realSize  = end - start

    // Shortcuts
    when (realSize) {
      0    -> return DoubleDeque()
      1    -> {
        val out = DoubleDeque(1)
        out.data[0] = data[internalIndex(start)]
        out.size = 1
        return out
      }
      size -> return copy()
    }

    val realStart = internalIndex(start)
    val realEnd   = internalIndex(end)

    val out = DoubleDeque(realSize)
    out.size = realSize

    // If the values are inline, we can just arraycopy out
    if (realStart < realEnd) {
      data.copyInto(out.data, 0, realStart, realEnd)
    }

    // The values are out of line, we have to do 2 copies
    else {
      data.copyInto(out.data, 0, realStart, data.size)
      data.copyInto(out.data, data.size - realStart, 0, realEnd)
    }

    return out
  }

  override fun slice(range: IntRange) = slice(range.first, range.last+1)

  override fun sliceToArray(start: Int, end: Int): DoubleArray {
    // If they gave us one or more invalid indices, throw an exception
    if (start !in 0 until size || start > end || end > size)
      throw IndexOutOfBoundsException()

    val realSize  = end - start

    // Shortcuts
    when (realSize) {
      0    -> return DoubleArray(0)
      1    -> return DoubleArray(1) { data[internalIndex(start)] }
      size -> return toArray()
    }

    val realStart = internalIndex(start)
    val realEnd   = internalIndex(end)

    val out = DoubleArray(realSize)

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

  override fun compact() = copyElements(cap)

  override fun trimToSize() = copyElements(size)

  override fun toString() = "DoubleDeque($size:$cap)"

  override fun equals(other: Any?) = if (other is DoubleDeque) data.contentEquals(other.data) else false

  override fun hashCode() = data.contentHashCode()

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
    data.copyInto(new, 0, realHead, data.size)
    data.copyInto(new, data.size - realHead, 0, realHead)
    realHead = 0
    data = new
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
    fun of(vararg values: Double) = DoubleDeque(values)
  }
}