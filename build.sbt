ThisBuild / scalaVersion := "2.13.16"
ThisBuild / sbtVersion   := "1.9.4"

lazy val root = (project in file("."))
  .settings(
    name := "bpbear-backend",
    version := "0.1.0",
    organization := "app.bpbear",

    // Compiler options
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-encoding", "utf8",
      "-Xfatal-warnings",
      "-Ywarn-unused",
      "-language:higherKinds",
      "-language:implicitConversions"
    ),

    libraryDependencies ++= Seq(
      // ZIO Core
      "dev.zio" %% "zio" % "2.1.22",
      "dev.zio" %% "zio-test" % "2.1.22" % Test,
      "dev.zio" %% "zio-test-sbt" % "2.1.22" % Test,
//      "dev.zio" %% "zio-logging-slf4j" % "2.3.0",

      // ZIO Config (compatible with ZIO 2.1.1)
      "dev.zio" %% "zio-config"            % "4.0.2",
      "dev.zio" %% "zio-config-magnolia"   % "4.0.2",
      "dev.zio" %% "zio-config-typesafe"   % "4.0.2",
      "dev.zio" %% "zio-config-refined"    % "4.0.2",

      // HTTP Server
      "dev.zio" %% "zio-http" % "3.3.3",

      // JSON
//      "dev.zio" %% "zio-json" % "0.7.44",

      // Doobie (Postgres + Hikari)
//      "org.tpolecat" %% "doobie-core"     % "1.0.0-RC9",
//      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC9",
//      "org.tpolecat" %% "doobie-hikari"   % "1.0.0-RC9",

      // ZIO ↔ Cats Effect for Doobie integration
//      "dev.zio" %% "zio-interop-cats" % "23.1.0.5",

      // Flyway migrations
//      "org.flywaydb" % "flyway-core" % "10.7.1",

      // BCrypt for password hashing
//      "org.mindrot" % "jbcrypt" % "0.4",

      // Logging backend
//      "ch.qos.logback" % "logback-classic" % "1.4.11"
    ),

    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
