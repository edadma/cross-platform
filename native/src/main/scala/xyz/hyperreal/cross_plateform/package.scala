package xyz.hyperreal

import java.io.FileWriter
import java.nio.file.{Files, Paths}

package object cross_plateform {

  def processArgs(a: Array[String]): Seq[String] = a

  def nameSeparator: String = System.getProperty("file.separator")

  def readFile(file: String): String = util.Using(io.Source.fromFile(file))(_.mkString).get

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

}
