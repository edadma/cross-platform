package xyz.hyperreal

import scala.scalajs.js
import js.Dynamic.{global => g}

package object cross_plateform {

  private val fs = g.require("fs")

  def readFile(file: String): String = fs.readFileSync(file).toString

}
