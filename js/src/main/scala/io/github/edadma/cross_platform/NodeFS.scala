package io.github.edadma.cross_platform

import scala.scalajs.js
import scala.scalajs.js.annotation._

// Define a facade for the fs module
@js.native
@JSImport("fs", JSImport.Namespace)
object NodeFS extends js.Object {
  def readFileSync(path: String): String                 = js.native
  def appendFileSync(path: String, data: String): String = js.native
  def writeFileSync(path: String, data: String): Unit    = js.native
  def existsSync(path: String): Boolean                  = js.native
  def accessSync(path: String, mode: Int): Unit          = js.native
  def readdirSync(path: String): js.Array[String]        = js.native
}
