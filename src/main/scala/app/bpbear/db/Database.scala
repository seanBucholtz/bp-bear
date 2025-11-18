package app.bpbear.db

import zio.*
import doobie.*
import doobie.hikari.*
import doobie.util.transactor.Transactor

final case class DbConfig(
                           url: String,
                           user: String,
                           password: String,
                           driver: String
                         )

object Database {

  val live: ZLayer[DbConfig, Throwable, Transactor[Task]] =
    ZLayer {
      for {
        config <- ZIO.service[DbConfig]
        xa <- HikariTransactor.newHikariTransactor[Task](
          driverClassName = config.driver,
          url = config.url,
          user = config.user,
          pass = config.password
        )
      } yield xa
    }
}
