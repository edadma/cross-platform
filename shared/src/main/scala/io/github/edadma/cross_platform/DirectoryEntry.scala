package io.github.edadma.cross_platform

sealed trait FileType
object FileType {
  case object File         extends FileType
  case object Directory    extends FileType
  case object SymbolicLink extends FileType
  case object Other        extends FileType
}

case class DirectoryEntry(name: String, fileType: FileType)
