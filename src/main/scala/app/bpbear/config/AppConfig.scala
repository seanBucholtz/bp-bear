package app.bpbear.config

import zio._
//import zio.config._
//import zio.config.typesafe.TypesafeConfigProvider

final case class AppConfig(serverPort: Int, serverHost: String)

object AppConfig {

  // Define the config schema manually (ZIO Config 4.x)
  val config: Config[AppConfig] =
    (Config.int("serverPort") zip Config.string("serverHost"))
      .map { case (port, host) => AppConfig(port, host) }

  // Layer that loads the config from application.conf
  val live: ZLayer[Any, Config.Error, AppConfig] =
    ZLayer.fromZIO(ZIO.config(config))
}
