package app.bpbear.api

import zio.*
import zio.http.*
import zio.json.*
import app.bpbear.repo.*
import app.bpbear.domain.*

object ReadingsApi {

  val routes: HttpApp[ReadingRepo, Throwable] =
    Http.collectZIO[Request] {
      case req@Method.GET -> !! / "readings" / userIdStr =>
        for {
          userId <- ZIO.attempt(userIdStr.toLong)
          repo <- ZIO.service[ReadingRepo]
          readings <- repo.getByUser(userId)
        } yield Response.json(readings.toJson)
    }
}
