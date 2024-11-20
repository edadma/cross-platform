package io.github.edadma.cross_platform

import java.io.FileWriter
import java.nio.file.{Files, Paths}

import scala.jdk.CollectionConverters._

def processArgs(a: Seq[String]): IndexedSeq[String] = a.toIndexedSeq

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

def listFiles(directory: String): Seq[String] = {
  val dirPath = Paths.get(directory)
  if (Files.isDirectory(dirPath)) {
    Files.list(dirPath)
      .iterator()
      .asScala
      .map(_.toAbsolutePath.normalize.toString)
      .toSeq
      .sorted
  } else {
    throw new IllegalArgumentException(s"$directory is not a directory or does not exist")
  }
}

def stdout(s: String): Unit = print(s)
