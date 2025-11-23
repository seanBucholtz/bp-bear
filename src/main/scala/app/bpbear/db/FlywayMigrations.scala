//package app.bpbear.db
//
//import zio._
//import org.flywaydb.core.Flyway
//
//object FlywayMigrations {
//
//  def runMigrations(dbConfig: DbConfig): Task[Unit] =
//    ZIO.attempt {
//      val flyway = Flyway.configure()
//        .dataSource(dbConfig.url, dbConfig.user, dbConfig.password)
//        .locations("classpath:db/migration")
//        .load()
//
//      flyway.migrate()
//    }
//}
