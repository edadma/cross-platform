package io.github.edadma.cross_platform

import scala.scalajs.js
import scala.scalajs.js.DynamicImplicits.*

import js.Dynamic.{global => g}

def processArgs(a: Seq[String]): IndexedSeq[String] =
  g.process.argv.asInstanceOf[js.Array[String]] drop 2 toIndexedSeq

private val fs      = g.require("fs")
private val path    = g.require("path")
private val process = js.Dynamic.global.process

def nameSeparator: String = path.sep.toString

def readFile(file: String): String = fs.readFileSync(file).toString

def writeFile(file: String, data: String): Unit = fs.writeFileSync(file, data)

def appendFile(file: String, data: String): Unit = fs.appendFileSync(file, data)

def readableFile(file: String): Boolean =
  try {
    fs.accessSync(file, fs.constants.R_OK)
    true
  } catch {
    case _: Exception => false
  }

def listFiles(directory: String): Seq[String] = {
  if (js.Dynamic.global.require != js.undefined) {
    val dirPath = path.resolve(directory)

    if (fs.existsSync(dirPath)) {
      fs.readdirSync(dirPath)
        .asInstanceOf[js.Array[String]]
        .map(file => path.resolve(dirPath, file).toString)
        .sort()
        .toList
    } else {
      throw new IllegalArgumentException(s"$directory is not a directory or does not exist")
    }
  } else {
    throw new UnsupportedOperationException("File system access is only available in Node.js")
  }
}

def stdout(s: String): Unit = process.stdout.write(s)

def processExit(code: Int): Nothing =
  process.exit(code)
  throw new RuntimeException("Unreachable code after process.exit")

def exists(path: String): Boolean =
  fs.existsSync(path).asInstanceOf[Boolean]

def isFile(path: String): Boolean = {
  if (!fs.existsSync(path).asInstanceOf[Boolean]) false
  else {
    val stats = fs.statSync(path)
    stats.isFile().asInstanceOf[Boolean]
  }
}

def isDirectory(path: String): Boolean = {
  if (!fs.existsSync(path).asInstanceOf[Boolean]) false
  else {
    val stats = fs.statSync(path)
    stats.isDirectory().asInstanceOf[Boolean]
  }
}

def readBytes(path: String): Array[Byte] = {
  val buffer     = fs.readFileSync(path)
  val uint8Array = js.Dynamic.newInstance(js.Dynamic.global.Uint8Array)(buffer)
  val array      = new Array[Byte](uint8Array.length.asInstanceOf[Int])
  for (i <- array.indices) {
    array(i) = uint8Array.selectDynamic(i.toString).asInstanceOf[Int].toByte
  }
  array
}

def writeBytes(path: String, data: Array[Byte]): Unit = {
  val uint8Array = js.Dynamic.newInstance(js.Dynamic.global.Uint8Array)(data.length)
  for (i <- data.indices) {
    uint8Array.update(i, data(i) & 0xff)
  }
  fs.writeFileSync(path, uint8Array)
}

def listDirectoryWithTypes(path: String): Vector[DirectoryEntry] = {
  if (!fs.existsSync(path).asInstanceOf[Boolean]) {
    throw new IllegalArgumentException(s"Path does not exist: $path")
  }

  val files = fs.readdirSync(path).asInstanceOf[js.Array[String]]
  files.toVector.map { name =>
    val fullPath = g.require("path").join(path, name).toString
    val stats    = fs.statSync(fullPath)
    val fileType = if (stats.isDirectory().asInstanceOf[Boolean]) FileType.Directory
    else if (stats.isSymbolicLink().asInstanceOf[Boolean]) FileType.SymbolicLink
    else if (stats.isFile().asInstanceOf[Boolean]) FileType.File
    else FileType.Other
    DirectoryEntry(name, fileType)
  }
}

def createDirectory(path: String): Unit =
  fs.mkdirSync(path)

def createDirectories(path: String): Unit =
  fs.mkdirSync(path, js.Dynamic.literal(recursive = true))

def deleteFile(path: String): Unit = {
  if (isDirectory(path)) {
    fs.rmdirSync(path)
  } else {
    fs.unlinkSync(path)
  }
}

def copyFile(source: String, target: String): Unit =
  fs.copyFileSync(source, target)

def moveFile(source: String, target: String): Unit =
  fs.renameSync(source, target)

def fileSize(path: String): Long = {
  val stats = fs.statSync(path)
  stats.size.asInstanceOf[Double].toLong
}

def lastModified(path: String): Long = {
  val stats = fs.statSync(path)
  stats.mtime.getTime().asInstanceOf[Double].toLong
}
