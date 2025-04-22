package io.github.edadma.cross_platform

import java.nio.file.{FileSystems, Files, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.*

def processArgs(a: Seq[String]): IndexedSeq[String] = a.toIndexedSeq

def nameSeparator: String = FileSystems.getDefault.getSeparator

def readFile(file: String): String = Files.readString(Paths.get(file))

def writeFile(file: String, data: String): Unit =
  Files.writeString(Paths.get(file), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

def appendFile(file: String, data: String): Unit =
  Files.writeString(Paths.get(file), data, StandardOpenOption.CREATE, StandardOpenOption.APPEND)

def readableFile(file: String): Boolean = {
  val path = Paths.get(file)

  Files.isReadable(path) && Files.isRegularFile(path)
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

def processExit(code: Int): Nothing = sys.exit(code)
