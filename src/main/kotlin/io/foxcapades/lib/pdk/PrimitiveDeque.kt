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
   */
  protected var realHead = 0

  /**
   * Number of elements in this deque.
   */
  abstract val size: Int

  /**
   * Currently allocated capacity.
   *
   * Values may be added to this deque until [size] == [cap] before the deque
   * will reallocate a larger backing buffer.
   *
   * This value will always be greater than or equal to [size].
   */
  abstract val cap: Int

  /**
   * Whether this deque is empty (contains no elements).
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
   */
  abstract fun clear()

  /**
   * Clears all elements from this deque, leaving it empty, but with the same
   * allocated capacity.
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
   * underlying container will be resized to [minCapacity].
   *
   * If the current capacity of this deque is already greater than or equal to
   * the given value, this method does nothing.
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
  abstract fun copyInto(array: A, offset: Int = 0)

  // region Internals

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

  protected fun newCap(old: Int, min: Int): Int {
    val new = old + (old shr 1)

    return when {
      new < min -> min
      else      -> new
    }
  }

  // endregion Internals
}