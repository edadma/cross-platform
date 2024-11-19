package io.github.edadma.cross_platform

import scala.scalajs.js
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
    writeFile(file, "")
    fs.accessSync(file, fs.constants.W_OK)
    true
  } catch {
    case _: Exception => false
  }
