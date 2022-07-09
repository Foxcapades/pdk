package io.foxcapades.lib.pdk


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
