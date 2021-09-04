package io.github.edadma

import java.nio.file.{Files, Paths, FileSystems}

package object cross_platform {

  def processArgs(a: Array[String]): Seq[String] = a

  def nameSeparator: String = FileSystems.getDefault.getSeparator

  def readFile(file: String): String = Files.readString(Paths.get(file))

  def writeFile(file: String, data: String): Unit = Files.writeString(Paths.get(file), data)

  def readableFile(file: String): Boolean = {
    val path = Paths.get(file)

    Files.isReadable(path) && Files.isRegularFile(path)
  }

  def writableFile(file: String): Boolean = {
    val path = Paths.get(file)

    Files.createFile(path)
    Files.isWritable(path) && Files.isRegularFile(path)
  }

}
