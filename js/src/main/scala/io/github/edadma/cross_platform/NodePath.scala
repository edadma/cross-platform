package io.github.edadma.cross_platform

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSImport("path", JSImport.Namespace)
object NodePath extends js.Object {
  val sep: String                     = js.native
  def resolve(paths: String*): String = js.native
  def join(paths: String*): String    = js.native
}
