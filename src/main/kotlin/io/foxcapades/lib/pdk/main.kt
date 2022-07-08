package io.foxcapades.lib.pdk

import java.io.FileInputStream
import java.nio.ByteBuffer

val chunkSize = 8192
val bytes = 2

fun byteBuffer() {
  val input  = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteBuffer.allocate(chunkSize)
  var total  = 0

  val start = System.currentTimeMillis()
  while (true) {
    val red = input.read(buffer.array())

    (0 until red step bytes).forEach {
      total += buffer.short
    }

    buffer.clear()
    if (red < chunkSize)
      break
  }

  println(System.currentTimeMillis() - start)
  println(total)
}

fun byteDeque() {
  val input  = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteDeque(chunkSize)
  var total  = 0

  val start = System.currentTimeMillis()
  while (true) {
    val red = buffer.fillFrom(input)

    (0 until red step bytes).forEach {
      total += buffer.popShort(false)
    }

    buffer.clear()
    if (red < chunkSize)
      break
  }
  println(System.currentTimeMillis() - start)
  println(total)
}

fun rawArray() {
  val input  = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteArray(chunkSize)
  var total  = 0

  val start = System.currentTimeMillis()
  while (true) {
    val red = input.read(buffer)

    (0 until red step bytes).forEach {
      total += buffer.toShort(it)
    }

    if (red < chunkSize)
      break
  }
  println(System.currentTimeMillis() - start)
  println(total)
}

fun main() {
  byteBuffer()
  byteDeque()
  rawArray()
}

inline fun ByteArray.toShort(off: Int) =
  ((this[off+1].toInt()  and 0xFF)  or
  ((this[off].toInt() and 0xFF) shl 8)).toShort()

inline fun ByteArray.toInt(off: Int) =
  (this[off+3].toInt()  and 0xFF)  or
  ((this[off+2].toInt() and 0xFF) shl 8)  or
  ((this[off+1].toInt() and 0xFF) shl 16) or
  ((this[off].toInt() and 0xFF) shl 24)

inline fun ByteArray.toLong(off: Int) =
  (this[off+7].toLong()  and 0xFFL)  or
  ((this[off+6].toLong() and 0xFFL) shl 8)  or
  ((this[off+5].toLong() and 0xFFL) shl 16) or
  ((this[off+4].toLong() and 0xFFL) shl 24) or
  ((this[off+3].toLong() and 0xFFL) shl 32) or
  ((this[off+2].toLong() and 0xFFL) shl 40) or
  ((this[off+1].toLong() and 0xFFL) shl 48) or
  ((this[off].toLong() and 0xFFL) shl 56)
