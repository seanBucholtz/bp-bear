package app.bpbear

import zio._
import zio.http._
import app.bpbear.config.AppConfig
import zio.config.typesafe.TypesafeConfigProvider

object Application extends ZIOAppDefault {

  private val homeRoute: Route[Any, Nothing] =
    Method.GET / Root -> handler(Response.text("Hello World!"))

  private val jsonRoute: Route[Any, Nothing] =
    Method.GET / "json" -> handler(Response.json("""{"greetings": "Hello World!"}"""))

  private val routes = Routes(homeRoute, jsonRoute)

  // Explicit type parameter needed for Scala 2.13
  private val serverConfigLayer: ZLayer[AppConfig, Nothing, Server.Config] =
    ZLayer.fromFunction((appConfig: AppConfig) =>
      Server.Config.default.binding(appConfig.serverHost, appConfig.serverPort)
    )

  // Compose all layers; include Config.Error for AppConfig.live
  private val appLayers: ZLayer[Any, Throwable, Server & AppConfig] =
    ZLayer.make[Server & AppConfig](
      AppConfig.live,       // ZLayer[Any, Config.Error, AppConfig]
      serverConfigLayer,    // ZLayer[AppConfig, Nothing, Server.Config]
      Server.live           // ZLayer[Server.Config, Throwable, Server & Driver]
    )

  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Runtime.setConfigProvider(TypesafeConfigProvider.fromResourcePath())

  override val run: ZIO[Any, Throwable, Unit] =
    (for {
      config <- ZIO.service[AppConfig]                     // get config
      _      <- Console.printLine(
        s"Server starting on http://${config.serverHost}:${config.serverPort}"
      )
      _      <- Server.serve(routes)                       // start HTTP server
    } yield ()).provideLayer(appLayers)                     // use provideLayer instead of provide
}
