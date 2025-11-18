ThisBuild / scalaVersion := "2.13.16"
ThisBuild / sbtVersion   := "1.9.4"

lazy val root = (project in file("."))
  .settings(
    name := "bpbear-backend",
    version := "0.1.0",
    organization := "app.bpbear",

    // Compiler options
    scalacOptions ++= Seq(
      "-deprecation",             // Warn about deprecated APIs
      "-feature",                 // Warn about misused language features
      "-unchecked",               // Additional warnings where generated code depends on assumptions
      "-encoding", "utf8",        // UTF-8 encoding
      "-Xfatal-warnings",         // Fail build on warnings
      "-Ywarn-unused",            // Warn on unused code
      "-language:higherKinds",    // Enable higher-kinded types
      "-language:implicitConversions"
    ),

    // Library dependencies
    libraryDependencies ++= Seq(
      // ZIO Core
      "dev.zio" %% "zio" % "2.1.1",
      "dev.zio" %% "zio-test" % "2.1.1" % Test,
      "dev.zio" %% "zio-test-sbt" % "2.1.1" % Test,
      "dev.zio" %% "zio-logging-slf4j" % "2.3.0",

      // HTTP Server
      "dev.zio" %% "zio-http" % "3.0.0-RC4",

      // JSON
      "dev.zio" %% "zio-json" % "0.6.2",

      // Doobie (Postgres + Hikari + ZIO)
      "org.tpolecat" %% "doobie-core"     % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-hikari"   % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-zio"      % "1.0.0-RC4",

      // Flyway for database migrations
      "org.flywaydb" % "flyway-core" % "10.7.1",

      // Logging backend
      "ch.qos.logback" % "logback-classic" % "1.4.11"
    ),

    // Test settings
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
