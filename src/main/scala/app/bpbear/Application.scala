package app.bpbear

import zio._
import zio.http._
import app.bpbear.config.AppConfig
import zio.config.typesafe.TypesafeConfigProvider

object Application extends ZIOAppDefault {

  // Bootstrap: ensure application.conf is read
  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Runtime.setConfigProvider(TypesafeConfigProvider.fromResourcePath())

  // Routes
  private val homeRoute: Route[Any, Nothing] =
    Method.GET / Root -> handler(Response.text("Hello World!"))

  private val jsonRoute: Route[Any, Nothing] =
    Method.GET / "json" -> handler(Response.json("""{"greetings": "Hello World!"}"""))

  private val routes = Routes(homeRoute, jsonRoute)

  // Layer: convert AppConfig to Server.Config
  private val serverConfigLayer: ZLayer[AppConfig, Throwable, Server.Config] =
    ZLayer.fromFunction((appConfig: AppConfig) =>
      Server.Config.default.binding(appConfig.serverHost, appConfig.serverPort)
    )

  // Compose all layers
  private val appLayers: ZLayer[Any, Throwable, Server & AppConfig] =
    ZLayer.make[Server & AppConfig](
      AppConfig.live.mapError(e => new RuntimeException(e.toString)),
      serverConfigLayer,
      Server.live
    )

  // Run server
  override val run: ZIO[Any, Throwable, Unit] =
    (for {
      config <- ZIO.service[AppConfig]
      _      <- Console.printLine(
        s"Server starting on http://${config.serverHost}:${config.serverPort}"
      )
      _      <- Server.serve(routes)
    } yield ()).provideLayer(appLayers)
}