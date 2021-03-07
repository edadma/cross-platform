package xyz.hyperreal

package object cross_plateform {

  def readFile(file: String): String = util.Using(io.Source.fromFile(file))(_.mkString).get

}
