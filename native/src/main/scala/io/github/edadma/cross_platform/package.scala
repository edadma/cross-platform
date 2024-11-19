package io.github.edadma.cross_platform

import java.io.FileWriter
import java.nio.file.{Files, Paths}

def processArgs(a: Array[String]): IndexedSeq[String] = a.toIndexedSeq

def nameSeparator: String = System.getProperty("file.separator")

def readFile(file: String): String = Files.readString(Paths.get(file))

def writeFile(file: String, data: String): Unit = {
  val f = new FileWriter(file)

  f.write(data)
  f.close()
}

def readableFile(file: String): Boolean = {
  val path = Paths.get(file)

  Files.isReadable(path) && Files.isRegularFile(path)
}

def writableFile(file: String): Boolean = {
  val path = Paths.get(file)

  Files.createFile(path)
  Files.isWritable(path) && Files.isRegularFile(path)
}
