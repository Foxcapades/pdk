package io.foxcapades.lib.pdk

import java.io.FileInputStream
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

val chunkSize = 8192

fun byteBuffer() {
  val input = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteBuffer.allocate(chunkSize)

  var i = 0L
  val start = System.currentTimeMillis()
  while (true) {
    val red = input.read(buffer.array())

    try {
      while (true) {
        buffer.long
        i++
      }
    } catch (e: BufferUnderflowException) {}

    buffer.clear()
    if (red < chunkSize)
      break
  }

  println(System.currentTimeMillis() - start)
  println(i)
}

fun byteDeque() {
  val input = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteDeque(chunkSize)

  var i = 0L
  val start = System.currentTimeMillis()
  while (true) {
    val red = buffer.fillFrom(input)

    while (buffer.size >= 8) {
      buffer.popLong(false)
      i++
    }

    buffer.clear()
    if (red < chunkSize)
      break
  }
  println(System.currentTimeMillis() - start)
  println(i)
}

fun rawArray() {
  val input = FileInputStream("/home/ellie/code/mine/jvm/lib/pdk/data.bin")
  val buffer = ByteArray(chunkSize)

  var i = 0L
  val start = System.currentTimeMillis()
  while (true) {
    val red = input.read(buffer)

    (0 until chunkSize step 8).forEach {
      (buffer[it].toLong()  and 0xFFL)         or
      ((buffer[it+1].toLong() and 0xFFL) shl 8)  or
      ((buffer[it+2].toLong() and 0xFFL) shl 16) or
      ((buffer[it+3].toLong() and 0xFFL) shl 24) or
      ((buffer[it+4].toLong() and 0xFFL) shl 32) or
      ((buffer[it+5].toLong() and 0xFFL) shl 40) or
      ((buffer[it+6].toLong() and 0xFFL) shl 48) or
      ((buffer[it+7].toLong() and 0xFFL) shl 56)
      i++
    }

    if (red < chunkSize)
      break
  }
  println(System.currentTimeMillis() - start)
  println(i)
}

fun main() {
  byteBuffer()
  byteDeque()
  rawArray()
}
