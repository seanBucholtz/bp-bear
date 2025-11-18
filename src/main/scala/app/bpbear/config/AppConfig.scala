package app.bpbear.config

import zio.*
import zio.config.*
import zio.config.magnolia.DeriveConfigDescriptor
import zio.config.typesafe.TypesafeConfigSource

final case class AppConfig(
                            dbUrl: String,
                            dbUser: String,
                            dbPassword: String,
                            dbDriver: String,
                            serverPort: Int
                          )

object AppConfig {
  val descriptor = DeriveConfigDescriptor.descriptor[AppConfig]

  val live: ZLayer[Any, Throwable, AppConfig] =
    ZLayer {
      for {
        source <- ZIO.attempt(TypesafeConfigSource.fromDefaultLoader())
        config <- read(descriptor from source)
      } yield config
    }
}
