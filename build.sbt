ThisBuild / licenses += "ISC" -> url("https://opensource.org/licenses/ISC")
ThisBuild / versionScheme     := Some("semver-spec")

publish / skip := true

lazy val cross_platform = crossProject(JSPlatform, JVMPlatform, NativePlatform).in(file("."))
  .settings(
    name         := "cross-platform",
    version      := "0.0.2",
    scalaVersion := "3.5.2",
    scalacOptions ++=
      Seq(
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:postfixOps",
        "-language:implicitConversions",
        "-language:existentials",
        "-language:dynamics",
        "-Xasync",
      ),
    organization           := "io.github.edadma",
    githubOwner            := "edadma",
    githubRepository       := name.value,
    publishMavenStyle      := true,
    Test / publishArtifact := false,
    licenses += "ISC"      -> url("https://opensource.org/licenses/ISC"),
  ).jvmSettings(
    libraryDependencies += "org.scala-js" %% "scalajs-stubs" % "1.1.0" % "provided",
  ).nativeSettings(
  ).jsSettings(
    jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
//    Test / scalaJSUseMainModuleInitializer := true,
//    Test / scalaJSUseTestModuleInitializer := false,
    Test / scalaJSUseMainModuleInitializer := false,
    Test / scalaJSUseTestModuleInitializer := true,
    scalaJSUseMainModuleInitializer        := true,
  )
