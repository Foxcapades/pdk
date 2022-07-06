package io.foxcapades.lib.pdk

import java.io.InputStream

/**
 * Takes the first 2 bytes from the backing [ByteDeque] and translates them into
 * a [Short] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 2 bytes.  This means, when called on a `ByteDeque` instance
 * containing less than 2 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ the available byte has been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 2 bytes.  This exception will only be thrown _after_ the byte available in
 * the deque have been consumed.
 */
fun ByteDeque.popShort(littleEndian: Boolean = false): Short =
  if (littleEndian)
    ((pop().toInt() and 0xFF) or
    ((pop().toInt() and 0xFF) shl 8)).toShort()
  else
    (((pop().toInt() and 0xFF) shl 8) or
    (pop().toInt() and 0xFF)).toShort()

/**
 * Takes the first 4 bytes from the backing [ByteDeque] and translates them into
 * an [Int] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 4 bytes.  This means, when called on a `ByteDeque` instance
 * containing less than 4 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ the available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 4 bytes.  This exception will only be thrown _after_ the bytes available in
 * the deque have been consumed.
 */
fun ByteDeque.popInt(littleEndian: Boolean = false): Int =
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
 * Takes the first 8 bytes from the backing [ByteDeque] and translates them into
 * a [Long] value.
 *
 * This method makes no attempt to verify that the backing `ByteDeque` actually
 * contains at least 8 bytes.  This means, when called on a `ByteDeque` instance
 * containing less than 8 bytes, this method will cause a
 * [NoSuchElementException] to be thrown _after_ the available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 8 bytes.  This exception will only be thrown _after_ the bytes available in
 * the deque have been consumed.
 */
fun ByteDeque.popLong(littleEndian: Boolean = false): Long =
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
 * [NoSuchElementException] to be thrown _after_ the available byte has been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 2 bytes.  This exception will only be thrown _after_ the byte available in
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
 * [NoSuchElementException] to be thrown _after_ the available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 4 bytes.  This exception will only be thrown _after_ the bytes available in
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
 * [NoSuchElementException] to be thrown _after_ the available bytes have been
 * popped from the head of the deque.  Callers should verify the length of
 * the deque before attempting to call this method.
 *
 * @param littleEndian Boolean flag indicating whether the bytes in the deque
 * should be translated to an int with a little endian byte order.
 *
 * Defaults to `false` (big endian).
 *
 * @throws NoSuchElementException If called on a deque instance with fewer than
 * 8 bytes.  This exception will only be thrown _after_ the bytes available in
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
 * Reads bytes into the tail end of the given [ByteDeque] until either the input
 * stream has no more data or the given deque has no more space available.
 *
 * If the given deque has no room available (indicated by a [ByteDeque.space]
 * value of `0`) this method does nothing and returns `0` to indicate that no
 * bytes were read.
 *
 * This function is based on [InputStream.read] and thus will block until data
 * is available, until the end of the stream has been reached, or until an
 * exception is thrown.
 *
 * If the receiver stream has no data remaining when this function is called,
 * this function will return `-1` to indicate that there were no bytes in the
 * stream to be read.
 *
 * Otherwise, if the stream does have input and the given deque does have room,
 * this method returns the number of bytes read into the given deque.
 *
 * @param deque Deque into which data from the receiver [InputStream] will be
 * read.
 *
 * @return The total number of bytes read into the given deque, or `-1` to
 * indicate the end of the stream had already been reached.
 */
fun InputStream.read(deque: ByteDeque): Int {
  if (deque.space == 0)
    return 0

  var c = read()
  if (c == -1) {
    return -1
  }

  deque.pushTail(c.toByte())

  var count = 1
  while (deque.space > 0) {
    c = read()
    if (c == -1)
      break
    deque.pushTail(c.toByte())
    count++
  }

  return count
}