# Cross-Platform - Unified File System Operations for Scala

A lightweight library providing consistent file system and process operations across JVM, Scala.js, and Scala Native platforms.

## Why Cross-Platform?

Different Scala platforms have different APIs for basic operations like reading files or getting command line arguments. This library provides a single, consistent API that works identically across all platforms.

```scala
import io.github.edadma.cross_platform.*

// Same code works on JVM, JS, and Native
val content = readFile("config.json")
writeFile("output.txt", content)
val files = listFiles(".")
```

## Installation

Add to your `build.sbt`:

```scala
libraryDependencies += "io.github.edadma" %%% "cross-platform" % "0.0.6"
```

For cross-platform projects:
```scala
lazy val myProject = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .settings(
    libraryDependencies += "io.github.edadma" %%% "cross-platform" % "0.0.6"
  )
```

## Core Operations

### File Operations
```scala
// Text files
val content = readFile("data.txt")
writeFile("output.txt", "Hello, World!")
appendFile("log.txt", "New entry\n")

// Binary files
val data = readBytes("image.png")
writeBytes("copy.png", data)

// File information
val exists = exists("file.txt")
val isFile = isFile("document.pdf")
val isDir = isDirectory("folder")
val size = fileSize("data.txt")
val modified = lastModified("file.txt")
```

### Directory Operations
```scala
// List directory contents
val files = listFiles("/home/user")  // Returns file paths
val entries = listDirectoryWithTypes(".")  // Returns DirectoryEntry with types

entries.foreach { entry =>
  entry.fileType match {
    case FileType.File => println(s"File: ${entry.name}")
    case FileType.Directory => println(s"Dir: ${entry.name}")
    case FileType.SymbolicLink => println(s"Link: ${entry.name}")
    case FileType.Other => println(s"Other: ${entry.name}")
  }
}

// Create directories
createDirectory("new-folder")
createDirectories("path/to/nested/folders")  // Creates parent dirs
```

### File Management
```scala
// Copy and move files
copyFile("source.txt", "backup.txt")
moveFile("temp.txt", "archive/temp.txt")

// Delete files and directories
deleteFile("unwanted.txt")
deleteFile("empty-directory")  // Works for both files and dirs
```

### Process Operations
```scala
// Command line arguments (cross-platform)
val args = processArgs(args)  // Pass main method args

// Output
stdout("Hello, World!")

// Exit
processExit(0)  // or processExit(1) for error
```

### Path Utilities
```scala
// Get platform-specific path separator
val sep = nameSeparator  // "/" on Unix, "\" on Windows

// Check file permissions
val canRead = readableFile("file.txt")
```

## Platform Implementations

The library automatically uses the best implementation for each platform:

- **JVM**: Uses `java.nio.files` for robust, high-performance operations
- **Scala.js**: Uses Node.js `fs` and `path` modules for file operations
- **Scala Native**: Uses Java NIO compatibility layer

Same API, optimized implementations.

## File Types

```scala
sealed trait FileType
object FileType {
  case object File extends FileType
  case object Directory extends FileType  
  case object SymbolicLink extends FileType
  case object Other extends FileType  // Device files, pipes, etc.
}

case class DirectoryEntry(name: String, fileType: FileType)
```

## Example: Configuration File Handler

```scala
import io.github.edadma.cross_platform.*

object ConfigManager {
  private val configFile = "app.config"
  
  def loadConfig(): String = {
    if (exists(configFile)) {
      readFile(configFile)
    } else {
      val defaultConfig = """{"theme": "dark", "version": "1.0"}"""
      writeFile(configFile, defaultConfig)
      defaultConfig
    }
  }
  
  def saveConfig(config: String): Unit = {
    writeFile(configFile, config)
  }
  
  def backupConfig(): Unit = {
    if (exists(configFile)) {
      val timestamp = System.currentTimeMillis()
      copyFile(configFile, s"$configFile.backup.$timestamp")
    }
  }
}
```

## Example: Directory Scanner

```scala
import io.github.edadma.cross_platform.*

def scanDirectory(path: String): Unit = {
  if (!exists(path) || !isDirectory(path)) {
    println(s"$path is not a valid directory")
    return
  }
  
  val entries = listDirectoryWithTypes(path)
  
  println(s"Contents of $path:")
  entries.foreach { entry =>
    val size = if (entry.fileType == FileType.File) {
      s" (${fileSize(path + nameSeparator + entry.name)} bytes)"
    } else ""
    
    val typeStr = entry.fileType match {
      case FileType.File => "FILE"
      case FileType.Directory => "DIR "
      case FileType.SymbolicLink => "LINK"
      case FileType.Other => "OTHER"
    }
    
    println(s"  $typeStr: ${entry.name}$size")
  }
}
```

## Error Handling

Operations may throw standard exceptions:
- `IllegalArgumentException` for invalid paths
- `UnsupportedOperationException` for platform limitations
- Platform-specific I/O exceptions for file system errors

## Contributing

This library focuses on providing essential cross-platform operations. Contributions should:

1. Work identically across all three platforms
2. Use platform-optimized implementations
3. Maintain the simple, consistent API
4. Include tests for all platforms

## License

ISC License - see LICENSE file for details.

---

*Write once, run everywhere - the way cross-platform file operations should work in Scala.*