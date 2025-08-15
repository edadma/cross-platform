package io.github.edadma.cross_platform

import java.nio.file.{FileSystems, Files, Paths, StandardCopyOption, StandardOpenOption}
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

def exists(path: String): Boolean =
  Files.exists(Paths.get(path))

def isFile(path: String): Boolean =
  Files.isRegularFile(Paths.get(path))

def isDirectory(path: String): Boolean =
  Files.isDirectory(Paths.get(path))

def readBytes(path: String): Array[Byte] =
  Files.readAllBytes(Paths.get(path))

def writeBytes(path: String, data: Array[Byte]): Unit =
  Files.write(Paths.get(path), data)

def listDirectoryWithTypes(path: String): Vector[DirectoryEntry] = {
  val javaPath = Paths.get(path)
  if (!Files.isDirectory(javaPath)) {
    throw new IllegalArgumentException(s"Path is not a directory: $path")
  }

  Files.list(javaPath).iterator().asScala.toVector.map { entry =>
    val name = entry.getFileName.toString
    val fileType = if (Files.isDirectory(entry)) FileType.Directory
    else if (Files.isSymbolicLink(entry)) FileType.SymbolicLink
    else if (Files.isRegularFile(entry)) FileType.File
    else FileType.Other
    DirectoryEntry(name, fileType)
  }
}

def createDirectory(path: String): Unit =
  Files.createDirectory(Paths.get(path))

def createDirectories(path: String): Unit =
  Files.createDirectories(Paths.get(path))

def deleteFile(path: String): Unit =
  Files.delete(Paths.get(path))

def copyFile(source: String, target: String): Unit =
  Files.copy(Paths.get(source), Paths.get(target), StandardCopyOption.REPLACE_EXISTING)

def moveFile(source: String, target: String): Unit =
  Files.move(Paths.get(source), Paths.get(target), StandardCopyOption.REPLACE_EXISTING)

def fileSize(path: String): Long =
  Files.size(Paths.get(path))

def lastModified(path: String): Long =
  Files.getLastModifiedTime(Paths.get(path)).toMillis
