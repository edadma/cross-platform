package io.github.edadma.cross_platform

import scala.scalajs.js
import scala.scalajs.js.DynamicImplicits.*

def processArgs(a: Seq[String]): IndexedSeq[String] =
  js.Dynamic.global.process.argv.asInstanceOf[js.Array[String]] drop 2 toIndexedSeq

private val process = js.Dynamic.global.process

def nameSeparator: String = NodePath.sep

def getCurrentDirectory: String = process.cwd().toString

def readFile(file: String): String = NodeFS.readFileSync(file)

def writeFile(file: String, data: String): Unit = NodeFS.writeFileSync(file, data)

def appendFile(file: String, data: String): Unit = NodeFS.appendFileSync(file, data)

def readableFile(file: String): Boolean = {
  NodeFS.accessSync(file, NodeFS.constants.R_OK)
  true
}

def listFiles(directory: String): Seq[String] = {
  if (NodeFS.existsSync(directory)) {
    NodeFS.readdirSync(directory)
      .asInstanceOf[js.Array[String]]
      .map(file => NodePath.resolve(directory, file).toString)
      .sort()
      .toList
  } else {
    throw new IllegalArgumentException(s"$directory is not a directory or does not exist")
  }
}

def stdout(s: String): Unit = process.stdout.write(s)

def processExit(code: Int): Nothing =
  process.exit(code)
  throw new RuntimeException("Unreachable code after process.exit")

def exists(path: String): Boolean =
  NodeFS.existsSync(path)

def isFile(path: String): Boolean = {
  if (!NodeFS.existsSync(path)) false
  else {
    val stats = NodeFS.statSync(path)
    stats.isFile()
  }
}

def isDirectory(path: String): Boolean = {
  if (!NodeFS.existsSync(path)) false
  else {
    val stats = NodeFS.statSync(path)
    stats.isDirectory()
  }
}

def isSymbolicLink(path: String): Boolean = {
  if (!NodeFS.existsSync(path)) false
  else {
    val stats = NodeFS.lstatSync(path) // Use lstat to get symlink info
    stats.isSymbolicLink()
  }
}

def isReadable(path: String): Boolean = {
  NodeFS.accessSync(path, NodeFS.constants.R_OK)
  true
}

def isWritable(path: String): Boolean = {
  NodeFS.accessSync(path, NodeFS.constants.W_OK)
  true
}

def isExecutable(path: String): Boolean = {
  NodeFS.accessSync(path, NodeFS.constants.X_OK)
  true
}

def isSameFile(path1: String, path2: String): Boolean = {
  if (!exists(path1) || !exists(path2)) false
  else {
    val stats1 = NodeFS.statSync(path1)
    val stats2 = NodeFS.statSync(path2)

    // Compare device and inode numbers (Unix-like) or use other unique identifiers
    val dev1 = stats1.dev
    val ino1 = stats1.ino
    val dev2 = stats2.dev
    val ino2 = stats2.ino

    dev1 == dev2 && ino1 == ino2
  }
}

def readBytes(path: String): Array[Byte] = {
  // Read as Buffer and convert to Array manually
  val buffer = NodeFS.readFileSync(path)
  val length = buffer.asInstanceOf[js.Dynamic].length.asInstanceOf[Int]
  val array  = new Array[Byte](length)
  for (i <- 0 until length) {
    array(i) = buffer.asInstanceOf[js.Dynamic].selectDynamic(i.toString).asInstanceOf[Int].toByte
  }
  array
}

def writeBytes(path: String, data: Array[Byte]): Unit = {
  // Convert Array[Byte] to Node.js Buffer
  val jsArray = js.Array(data.map(_ & 0xff)*)
  val buffer  = NodeBuffer.from(jsArray)
  NodeFS.writeFileSync(path, buffer)
}

def listDirectoryWithTypes(path: String): Vector[DirectoryEntry] = {
  if (!NodeFS.existsSync(path)) {
    throw new IllegalArgumentException(s"Path does not exist: $path")
  }

  val files = NodeFS.readdirSync(path)
  files.toVector.map { name =>
    val fullPath = NodePath.join(path, name)
    val stats    = NodeFS.lstatSync(fullPath) // Use lstat to detect symlinks properly
    val fileType = if (stats.isSymbolicLink()) FileType.SymbolicLink
    else if (stats.isDirectory()) FileType.Directory
    else if (stats.isFile()) FileType.File
    else FileType.Other
    DirectoryEntry(name, fileType)
  }
}

def createDirectory(path: String): Unit =
  NodeFS.mkdirSync(path)

def createDirectories(path: String): Unit =
  NodeFS.mkdirSync(path, js.Dynamic.literal(recursive = true))

def deleteFile(path: String): Unit = {
  if (isDirectory(path)) {
    NodeFS.rmdirSync(path)
  } else {
    NodeFS.unlinkSync(path)
  }
}

def copyFile(source: String, target: String): Unit =
  NodeFS.copyFileSync(source, target)

def moveFile(source: String, target: String): Unit =
  NodeFS.renameSync(source, target)

def fileSize(path: String): Long = {
  val stats = NodeFS.statSync(path)
  stats.size.toLong
}

def lastModified(path: String): Long = {
  val stats = NodeFS.statSync(path)
  stats.mtime.getTime().toLong
}
