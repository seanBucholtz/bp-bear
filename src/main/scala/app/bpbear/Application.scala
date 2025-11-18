package app.bpbear

import zio.*
import zio.http.*
import app.bpbear.api.*
import app.bpbear.db.*
import app.bpbear.repo.*
import app.bpbear.service.*

object Application extends ZIOAppDefault {

  // Read DB config from environment with defaults
  val dbConfig: DbConfig = DbConfig(
    url = sys.env.getOrElse("DB_URL", "jdbc:postgresql://localhost:5432/bpbear"),
    user = sys.env.getOrElse("DB_USER", "postgres"),
    password = sys.env.getOrElse("DB_PASSWORD", ""),
    driver = sys.env.getOrElse("DB_DRIVER", "org.postgresql.Driver")
  )

  // Read server port from environment
  val serverPort: Int = sys.env.getOrElse("SERVER_PORT", "8080").toInt

  // Combine all routes
  val routes: HttpApp[Any, Throwable] =
    ReadingsApi.routes ++
      UsersApi.routes ++
      AnalyticsApi.routes

  // Main HTTP app
  val app: HttpApp[Any, Throwable] = routes

  override def run: URIO[zio.ZEnv, ExitCode] = {

    val fullLayer =
      ZLayer.succeed(dbConfig) >>>
        Database.live >>>
        (
          UserRepo.live ++
            ReadingRepo.live
          ) >>>
        (
          UserService.live ++
            ReadingService.live ++
            AnalyticsService.live
          )

    Server
      .serve(app)
      .provide(
        Server.default.withPort(serverPort),
        fullLayer
      )
      .exitCode
  }
}
