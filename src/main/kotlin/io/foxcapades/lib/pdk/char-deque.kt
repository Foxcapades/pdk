package io.foxcapades.lib.pdk

/**
 * Copies the contents of the CharDeque into a new String value.
 *
 * @return String value consisting of the characters from the deque.
 */
fun CharDeque.stringValue(): String =
  String(toArray())