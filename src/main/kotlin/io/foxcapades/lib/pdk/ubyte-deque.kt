package io.foxcapades.lib.pdk

/**
 * Takes the first byte from the backing [UByteDeque] and translates it into a
 * [UByte] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 1 byte.  This means, when called on an empty `UByteDeque`
 * instance, this method will cause a [NoSuchElementException] to be thrown.
 * Callers should verify the length of the deque before attempting to call this
 * method.
 *
 * @throws NoSuchElementException If called on an empty deque instance.
 */
fun UByteDeque.popByte(): Byte = pop().toByte()

/**
 * Takes the first 2 bytes from the backing [UByteDeque] and translates them into
 * a [Short] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 2 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 2 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 2 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popShort(littleEndian: Boolean = false): Short =
  if (littleEndian)
    ((pop().toInt() and 0xFF) or
    ((pop().toInt() and 0xFF) shl 8)).toShort()
  else
    (((pop().toInt() and 0xFF) shl 8) or
    (pop().toInt() and 0xFF)).toShort()

/**
 * Takes the first 4 bytes from the backing [UByteDeque] and translates them into
 * an [Int] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 4 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 4 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popInt(littleEndian: Boolean = false): Int =
  if (littleEndian)
    (pop().toInt()  and 0xFF)         or
    ((pop().toInt() and 0xFF) shl 8)  or
    ((pop().toInt() and 0xFF) shl 16) or
    ((pop().toInt() and 0xFF) shl 24)
  else
    ((pop().toInt() and 0xFF) shl 24) or
    ((pop().toInt() and 0xFF) shl 16) or
    ((pop().toInt() and 0xFF) shl 8 ) or
    (pop().toInt()  and 0xFF)

/**
 * Takes the first 8 bytes from the backing [UByteDeque] and translates them into
 * a [Long] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 8 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 8 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popLong(littleEndian: Boolean = false): Long =
  if (littleEndian)
    (pop().toLong()  and 0xFFL)         or
    ((pop().toLong() and 0xFFL) shl 8)  or
    ((pop().toLong() and 0xFFL) shl 16) or
    ((pop().toLong() and 0xFFL) shl 24) or
    ((pop().toLong() and 0xFFL) shl 32) or
    ((pop().toLong() and 0xFFL) shl 40) or
    ((pop().toLong() and 0xFFL) shl 48) or
    ((pop().toLong() and 0xFFL) shl 56)
  else
    ((pop().toLong() and 0xFFL) shl 56) or
    ((pop().toLong() and 0xFFL) shl 48) or
    ((pop().toLong() and 0xFFL) shl 40) or
    ((pop().toLong() and 0xFFL) shl 32) or
    ((pop().toLong() and 0xFFL) shl 24) or
    ((pop().toLong() and 0xFFL) shl 16) or
    ((pop().toLong() and 0xFFL) shl 8)  or
    (pop().toLong()  and 0xFFL)

/**
 * Takes the first 2 bytes from the backing [UByteDeque] and translates them into
 * a [UShort] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 2 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 2 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 2 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popUShort(littleEndian: Boolean = false): UShort =
  if (littleEndian)
    ((pop().toInt() and 0xFF) or
    ((pop().toInt() and 0xFF) shl 8)).toUShort()
  else
    (((pop().toInt() and 0xFF) shl 8) or
    (pop().toInt() and 0xFF)).toUShort()

/**
 * Takes the first 4 bytes from the backing [UByteDeque] and translates them into
 * an [UInt] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 4 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 4 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popUInt(littleEndian: Boolean = false): UInt =
  if (littleEndian)
    (pop().toUInt()  and 0xFFu)         or
    ((pop().toUInt() and 0xFFu) shl 8)  or
    ((pop().toUInt() and 0xFFu) shl 16) or
    ((pop().toUInt() and 0xFFu) shl 24)
  else
    ((pop().toUInt() and 0xFFu) shl 24) or
    ((pop().toUInt() and 0xFFu) shl 16) or
    ((pop().toUInt() and 0xFFu) shl 8 ) or
    (pop().toUInt()  and 0xFFu)

/**
 * Takes the first 8 bytes from the backing [UByteDeque] and translates them into
 * a [ULong] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 8 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 8 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popULong(littleEndian: Boolean = false): ULong =
  if (littleEndian)
    (pop().toULong()  and 0xFFu)         or
    ((pop().toULong() and 0xFFu) shl 8)  or
    ((pop().toULong() and 0xFFu) shl 16) or
    ((pop().toULong() and 0xFFu) shl 24) or
    ((pop().toULong() and 0xFFu) shl 32) or
    ((pop().toULong() and 0xFFu) shl 40) or
    ((pop().toULong() and 0xFFu) shl 48) or
    ((pop().toULong() and 0xFFu) shl 56)
  else
    ((pop().toULong() and 0xFFu) shl 56) or
    ((pop().toULong() and 0xFFu) shl 48) or
    ((pop().toULong() and 0xFFu) shl 40) or
    ((pop().toULong() and 0xFFu) shl 32) or
    ((pop().toULong() and 0xFFu) shl 24) or
    ((pop().toULong() and 0xFFu) shl 16) or
    ((pop().toULong() and 0xFFu) shl 8)  or
    (pop().toULong()  and 0xFFu)

/**
 * Takes the first 4 bytes from the backing [UByteDeque] and translates them into
 * a [Float] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 4 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 4 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popFloat(littleEndian: Boolean = false): Float =
  Float.fromBits(popInt(littleEndian))

/**
 * Takes the first 8 bytes from the backing [UByteDeque] and translates them into
 * a [Double] value.
 *
 * This method makes no attempt to verify that the backing `UByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `UByteDeque` instance
 * containing less than 8 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ any available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 8 bytes.  This exception will only be thrown _after_ any bytes available in
 * the deque have been consumed.
 */
fun UByteDeque.popDouble(littleEndian: Boolean = false): Double =
  Double.fromBits(popLong(littleEndian))
