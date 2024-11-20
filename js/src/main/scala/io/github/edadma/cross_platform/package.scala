package io.github.edadma.cross_platform

import scala.scalajs.js
import scala.scalajs.js.DynamicImplicits.*

import js.Dynamic.{global => g}

def processArgs(a: Seq[String]): IndexedSeq[String] =
  g.process.argv.asInstanceOf[js.Array[String]] drop 2 toIndexedSeq

private val fs   = g.require("fs")
private val path = g.require("path")

def nameSeparator: String = path.sep.toString

def readFile(file: String): String = fs.readFileSync(file).toString

def writeFile(file: String, data: String): Unit = fs.writeFileSync(file, data)

def readableFile(file: String): Boolean =
  try {
    fs.accessSync(file, fs.constants.R_OK)
    true
  } catch {
    case _: Exception => false
  }

def writableFile(file: String): Boolean =
  try {
    fs.accessSync(file, fs.constants.W_OK)
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

def stdout(s: String): Unit = js.Dynamic.global.process.stdout(s)
