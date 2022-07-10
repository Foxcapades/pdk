package io.foxcapades.lib.pdk

/**
 * # Primitive Deque
 *
 * Base type for all the primitive deque types included in the pdk package.
 *
 * This type defines the shared values and functions common to all the deque
 * types without specifying a generic return type.
 *
 * Excluding the generic methods specified below, the types implementing this
 * abstract class will deal entirely in unboxed primitive values.
 *
 * ## Generic Methods
 *
 * While the primitive deque implementations deal internally in unboxed values
 * only, when interoperating with some standard library types, generics and
 * value boxing come in to play.
 *
 * The following methods are the only methods and functionality defined on the
 * [PrimitiveDeque] subtypes that deal in boxed types.
 *
 * * [iterator]
 * * [toList]
 *
 * @param V Boxed type of the value the primitive deque implementer will handle.
 *
 * This is used simply to provide return types on methods that do use generics,
 * such as [iterator] and [toList].
 *
 * @param A Type of the raw array that backs each of the types implementing this
 * abstract class.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since v1.0.0
 */
@Suppress("NOTHING_TO_INLINE")
sealed class PrimitiveDeque<V, A> : Iterable<V> {

  /**
   * Index of the 'first' element in the deque, which may or may not be the
   * first element in the backing array.
   *
   * When the deque is accessed by a given index (external index), the
   * [realHead] value is used to calculate the actual index (internal index).
   *
   * In the following examples, the `|` character represents the [realHead]
   * position and the value underneath it is the actual [realHead] value for the
   * example.
   *
   * *Compacted*
   * ```
   * Deque{1, 2, 3, 4, 5, 6, 0, 0, 0}
   *       |
   *       0
   * ```
   *
   * *Uncompacted*
   * ```
   * Deque{4, 5, 6, 0, 0, 0, 1, 2, 3}
   *                         |
   *                         6
   * ```
   */
  protected var realHead = 0

  /**
   * Number of elements in this deque.
   *
   * This value will always be less than or equal to the value of [cap].
   *
   * **Example**
   * ```
   * // Create a deque with an initial capacity (cap) value of `9`
   * val deque = Deque(9)        // deque == {0, 0, 0, 0, 0, 0, 0, 0, 0}
   *
   * // Deque size will be 0 as no elements have been inserted.
   * assert(deque.size == 0)
   *
   * // Add some elements to the deque
   * deque += [1, 2, 3, 4, 5, 6] // deque == {1, 2, 3, 4, 5, 6, 0, 0, 0}
   *
   * // Deque size will now be 6 as we appended 6 elements to the empty deque.
   * assert(deque.size == 6)
   * ```
   */
  abstract val size: Int

  /**
   * Currently allocated capacity.
   *
   * Values may be added to this deque until [size] == [cap] before the deque
   * will reallocate a larger backing buffer.
   *
   * This value will always be greater than or equal to [size].
   *
   * **Example**
   * ```
   * // Create a deque with an initial capacity (cap) value of `9`
   * val deque = Deque(9)        // deque == {0, 0, 0, 0, 0, 0, 0, 0, 0}
   *
   * // Even though we have not yet inserted any elements, the capacity is 9
   * assert(deque.cap == 9)
   *
   * // Add some elements to the deque
   * deque += [1, 2, 3, 4, 5, 6] // deque == {1, 2, 3, 4, 5, 6, 0, 0, 0}
   *
   * // Deque capacity will still be 9 as we have not yet inserted enough
   * // elements to require a capacity increase
   * assert(deque.cap == 9)
   *
   * // Increase the capacity to 12
   * deque.ensureCapacity(12)    // deque == {1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0, 0}
   * ```
   *
   * The backing buffer is always increased to at minimum hold the number of
   * values being inserted, but the rate of increase may scale the size of the
   * capacity faster than that.
   *
   * This is to avoid many repeated re-allocations of the backing container.
   * To put it simply, it would be very expensive to use a deque (or [ArrayList]
   * for that matter) if every new element required a resize.
   */
  abstract val cap: Int

  /**
   * The amount of space left in the currently allocated backing container for
   * this deque.
   *
   * Inserting a number of elements into this deque greater than [space] will
   * cause the deque's backing container to be resized to accommodate the new
   * values.
   *
   * This value is calculated as `cap - size`.
   */
  abstract val space: Int

  /**
   * Whether this deque is empty (contains no elements).
   *
   * This value is unrelated to the current [cap] value.
   *
   * This value is a convenience shortcut for `size == 0`
   */
  inline val isEmpty: Boolean
    get() = size == 0

  /**
   * Index of the last element in this deque
   *
   * If this deque is empty, this value will be -1.
   */
  inline val lastIndex: Int
    get() = size - 1

  /**
   * Clears all elements from this deque, leaving it empty, but with the same
   * allocated capacity.
   *
   * This operation happens in `O(1)` time.
   */
  abstract fun clear()

  /**
   * Clears all elements from this deque, leaving it empty, but with the same
   * allocated capacity.
   *
   * This operation happens in `O(1)` time.
   *
   * Alias for [clear]
   */
  inline fun reset() = clear()

  /**
   * Creates a copy of this deque and its data.
   *
   * The copied deque instance will have the same capacity as the original.
   */
  abstract fun copy(): PrimitiveDeque<V, A>

  /**
   * Ensures that this deque has at least the given capacity allocated.
   *
   * If the current capacity of this deque is less than the given value, the
   * underlying container will be resized to have a capacity of *at least*
   * [minCapacity].
   *
   * If the current capacity of this deque is already greater than or equal to
   * the given value, this method does nothing.
   *
   * @param minCapacity Minimum capacity this deque must have.
   */
  abstract fun ensureCapacity(minCapacity: Int)

  /**
   * Returns a new array containing the contents of this deque.
   *
   * The length of the returned array will be equal to [size].
   *
   * @return A new array containing the data from this deque.
   */
  abstract fun toArray(): A

  /**
   * Returns a new list containing the contents of this deque.
   *
   * The length of the returned list will be equal to [size].
   *
   * @return A new list containing the data from this deque.
   */
  abstract fun toList(): List<V>

  /**
   * Creates a new deque containing the elements from this deque that fall
   * within the specified index range.
   *
   * **Example**
   * ```
   * val deque = Deque{0, 1, 2, 3, 4, 5}
   *
   * deque.slice(1, 4) // Deque{1, 2, 3}
   * ```
   *
   * @param start Inclusive start position of the range of values to slice.
   *
   * @param end Exclusive end position of the range of values to slice.
   *
   * @return A new deque containing the elements from this deque that fall
   * within the given specified index range.
   *
   * @throws IndexOutOfBoundsException If [start] is less than `0`, if [end] is
   * greater than [size], or if [start] is greater than [end].
   */
  abstract fun slice(start: Int, end: Int = size): PrimitiveDeque<V, A>

  /**
   * Creates a new deque containing the elements from this deque that fall
   * within the given index range.
   *
   * **Example**
   * ```
   * val deque = Deque{0, 1, 2, 3, 4, 5}
   *
   * deque.slice(1..4) // Deque{1, 2, 3}
   * ```
   *
   * @param range Inclusive range of indices that will be sliced.
   *
   * @return A new deque containing the elements from this deque that fall
   * within the given specified index range.
   *
   * @throws IndexOutOfBoundsException If the start of the given range is less
   * than `0`, if the end of the given range is greater than or equal to [size],
   * or if the start of the given range is greater than the end.
   */
  abstract fun slice(range: IntRange): PrimitiveDeque<V, A>

  /**
   * Creates a new array containing the elements from this deque that fall
   * within the specified index range.
   *
   * **Example**
   * ```
   * val deque = Deque{0, 1, 2, 3, 4, 5}
   *
   * deque.sliceToArray(1, 4) // [1, 2, 3]
   * ```
   *
   * @param start Inclusive start position of the range of values to slice.
   *
   * @param end Exclusive end position of the range of values to slice.
   *
   * @return A new array containing the elements from this deque that fall
   * within the given specified index range.
   *
   * @throws IndexOutOfBoundsException If [start] is less than `0`, if [end] is
   * greater than [size], or if [start] is greater than [end].
   */
  abstract fun sliceToArray(start: Int, end: Int = size): A

  /**
   * Creates a new array containing the elements from this deque that fall
   * within the given index range.
   *
   * **Example**
   * ```
   * val deque = Deque{0, 1, 2, 3, 4, 5}
   *
   * deque.slice(1..4) // Deque{1, 2, 3}
   * ```
   *
   * @param range Inclusive range of indices that will be sliced.
   *
   * @return A new array containing the elements from this deque that fall
   * within the given specified index range.
   *
   * @throws IndexOutOfBoundsException If the start of the given range is less
   * than `0`, if the end of the given range is greater than or equal to [size],
   * or if the start of the given range is greater than the end.
   */
  abstract fun sliceToArray(range: IntRange): A

  /**
   * Optional operation that rearranges the data in the internal container to be
   * in a single contiguous block.
   *
   * This is particularly useful when preparing a deque to be used for repeated
   * reads from a stream source as it minimizes the number of internal reads
   * needed to fill the deque's backing container.
   *
   * The size, capacity, and publicly accessible data of this deque will not be
   * altered by this method.
   *
   * This operation happens in `O(n)` time, where `n` is the current deque
   * [cap].
   *
   * **Example**
   *
   * The following example describes the state of a deque containing the values
   * `1, 2, 3, 4, 5, 6` before and after calling the [compact] method.
   *
   * To create a deque that needs to be compacted, we will need to insert data
   * at both ends of the deque.
   * ```
   * // Create a deque with an initial capacity
   * val deque = Deque(9)
   *
   * // Append some elements to the end of the deque
   * deque += [4, 5, 6] // deque == {4, 5, 6, 0, 0, 0, 0, 0, 0}
   *
   * // Insert some elements at the front of the deque
   * deque.pushHead(3)  // deque == {4, 5, 6, 0, 0, 0, 0, 0, 3}
   * deque.pushHead(2)  // deque == {4, 5, 6, 0, 0, 0, 0, 2, 3}
   * deque.pushHead(1)  // deque == {4, 5, 6, 0, 0, 0, 1, 2, 3}
   *
   * // Compact the deque
   * deque.compact()    // deque == {1, 2, 3, 4, 5, 6, 0, 0, 0}
   * ```
   */
  abstract fun compact()

  /**
   * Trims the capacity of this deque to be the same as the current size.
   *
   * Callers can use this operation to minimize the storage used by a deque
   * instance to only the space necessary to hold [size] values.
   *
   * This operation is optional and is not necessary in most deque use cases.
   * Instances where many deques are in play, deques are pre-sized with large
   * initial capacities, and/or deques are held for long periods of time are
   * examples of situations where this action may be desirable.
   *
   * The data in the deque will be [compacted][compact] as part of this
   * operation.
   *
   * This operation happens in `O(n)` time, where `n` is the current deque
   * [size].
   *
   * **Examples**
   *
   * *Uncompacted*
   *
   * The following example describes the internal state change of an uncompacted
   * ("out of line") deque when [trimToSize] is called.
   *
   * ```
   * // Create our deque
   * val deque = Deque(9)
   *
   * // Populate it
   * populateDeque(deque) // deque == {4, 5, 6, 0, 0, 0, 1, 2, 3}
   *
   * // Trim it
   * deque.trimToSize()   // deque == {1, 2, 3, 4, 5, 6}
   * ```
   *
   * *Compacted*
   *
   * The following example describes the internal state change of a compacted
   * ("inline") deque when [trimToSize] is called.
   *
   * ```
   * // Create our deque
   * val deque = Deque(9)
   *
   * // Populate it
   * populateDeque(deque) // deque == {1, 2, 3, 4, 5, 6, 0, 0, 0}
   *
   * // Trim it
   * deque.trimToSize()   // deque == {1, 2, 3, 4, 5, 6}
   */
  abstract fun trimToSize()

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  abstract fun removeHead(count: Int = 1)

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeHead]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun removeFirst(count: Int = 1) = removeHead(count)

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeHead]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun removeFront(count: Int = 1) = removeHead(count)

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeHead]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteHead(count: Int = 1) = removeHead(count)

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeHead]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteFirst(count: Int = 1) = removeHead(count)

  /**
   * Removes the first [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeHead]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteFront(count: Int = 1) = removeHead(count)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  abstract fun removeTail(count: Int = 1)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeTail]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun removeLast(count: Int = 1) = removeTail(count)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeTail]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun removeBack(count: Int = 1) = removeTail(count)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeTail]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteTail(count: Int = 1) = removeTail(count)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeTail]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteLast(count: Int = 1) = removeTail(count)

  /**
   * Removes the last [count] elements of this deque.
   *
   * If this deque was empty, or if [count] equals `0`, this method does
   * nothing.
   *
   * If [count] is greater than or equal to [size], this method clears the deque
   * entirely (same as calling [clear]).
   *
   * This operation happens in `O(1)` time.
   *
   * Alias of [removeTail]
   *
   * @param count Number of elements to remove from this deque.
   *
   * Defaults to `1`.
   *
   * @throws IllegalArgumentException If [count] is less than `0`.
   */
  inline fun deleteBack(count: Int = 1) = removeTail(count)

  /**
   * Pushes the contents of the given array onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input array, the internal container will be resized to
   * accommodate the new values.
   *
   * @param values Array of values that will be pushed onto the back of this
   * deque.
   */
  abstract fun pushTail(values: A)

  /**
   * Pushes the contents of the given array onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input array, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Array of values that will be pushed onto the back of this
   * deque.
   */
  inline fun pushLast(values: A) = pushTail(values)

  /**
   * Pushes the contents of the given array onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input array, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Array of values that will be pushed onto the back of this
   * deque.
   */
  inline fun pushBack(values: A) = pushTail(values)

  /**
   * Pushes the contents of the given array onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input array, the internal container will be resized to
   * accommodate the new values.
   *
   * Alias of [pushTail]
   *
   * @param values Array of values that will be pushed onto the back of this
   * deque.
   */
  inline operator fun plusAssign(values: A) = pushTail(values)

  /**
   * Pushes the contents of the given [Collection] onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input `Collection`, the internal container will be resized
   * to accommodate the new values.
   *
   * This method iterates through the input collection to copy the values into
   * this deque.
   *
   * @param values `Collection` of values that will be pushed onto the back of
   * this deque.
   */
  abstract fun pushTail(values: Collection<V>)

  /**
   * Pushes the contents of the given [Collection] onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input `Collection`, the internal container will be resized
   * to accommodate the new values.
   *
   * This method iterates through the input collection to copy the values into
   * this deque.
   *
   * Alias of [pushTail]
   *
   * @param values `Collection` of values that will be pushed onto the back of
   * this deque.
   */
  inline fun pushLast(values: Collection<V>) = pushTail(values)

  /**
   * Pushes the contents of the given [Collection] onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input `Collection`, the internal container will be resized
   * to accommodate the new values.
   *
   * This method iterates through the input collection to copy the values into
   * this deque.
   *
   * Alias of [pushTail]
   *
   * @param values `Collection` of values that will be pushed onto the back of
   * this deque.
   */
  inline fun pushBack(values: Collection<V>) = pushTail(values)

  /**
   * Pushes the contents of the given [Collection] onto the back of this deque.
   *
   * If the capacity of this deque was less than the current deque size plus
   * the size of the input `Collection`, the internal container will be resized
   * to accommodate the new values.
   *
   * This method iterates through the input collection to copy the values into
   * this deque.
   *
   * Alias of [pushTail]
   *
   * @param values `Collection` of values that will be pushed onto the back of
   * this deque.
   */
  inline operator fun plusAssign(values: Collection<V>) = pushTail(values)

  /**
   * Ensures that the given external index is valid.
   */
  protected inline fun validExtInd(index: Int) =
    if (index < 0 || index > lastIndex)
      throw IndexOutOfBoundsException()
    else
      index

  protected inline fun positiveMod(i: Int) = if (i >= cap) i - cap else i

  protected inline fun negativeMod(i: Int) = if (i < 0) i + cap else i

  protected inline fun internalIndex(i: Int) = positiveMod(realHead + i)

  protected inline fun incremented(i: Int) = if (i == cap - 1) 0 else i + 1

  protected inline fun decremented(i: Int) = if (i == 0) cap - 1 else i - 1

  /**
   * Calculate a new capacity for the deque based on the given inputs.
   *
   * @param old Current deque capacity.
   *
   * @param min Minimum required capacity.
   *
   * @return The new capacity the deque should be reallocated to.
   */
  protected fun newCap(old: Int, min: Int): Int {
    val new = old + (old shr 1)

    return when {
      new < min -> min
      else      -> new
    }
  }
}