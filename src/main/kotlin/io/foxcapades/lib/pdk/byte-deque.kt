package io.foxcapades.lib.pdk

/**
 * Takes the first byte from the backing [ByteDeque] and translates it into a
 * [UByte] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 1 byte.  This means, when called on an empty `ByteDeque`
 * instance, this method will cause a [NoSuchElementException] to be thrown.
 * Callers should verify the length of the deque before attempting to call this
 * method.
 *
 * @throws NoSuchElementException If called on an empty deque instance.
 */
fun ByteDeque.popUByte(): UByte = pop().toUByte()

/**
 * Takes the first 2 bytes from the backing [ByteDeque] and translates them into
 * a [UShort] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 2 bytes.  This means, when called on a `ByteDeque` instance
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
fun ByteDeque.popUShort(littleEndian: Boolean = false): UShort =
  if (littleEndian)
    ((pop().toInt() and 0xFF) or
    ((pop().toInt() and 0xFF) shl 8)).toUShort()
  else
    (((pop().toInt() and 0xFF) shl 8) or
    (pop().toInt() and 0xFF)).toUShort()

/**
 * Takes the first 4 bytes from the backing [ByteDeque] and translates them into
 * an [UInt] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `ByteDeque` instance
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
fun ByteDeque.popUInt(littleEndian: Boolean = false): UInt =
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
 * Takes the first 8 bytes from the backing [ByteDeque] and translates them into
 * a [ULong] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `ByteDeque` instance
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
fun ByteDeque.popULong(littleEndian: Boolean = false): ULong =
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
 * Takes the first 4 bytes from the backing [ByteDeque] and translates them into
 * a [Float] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `ByteDeque` instance
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
fun ByteDeque.popFloat(littleEndian: Boolean = false): Float =
  Float.fromBits(popInt(littleEndian))

/**
 * Takes the first 8 bytes from the backing [ByteDeque] and translates them into
 * a [Double] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `ByteDeque` instance
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
fun ByteDeque.popDouble(littleEndian: Boolean = false): Double =
  Double.fromBits(popLong(littleEndian))
