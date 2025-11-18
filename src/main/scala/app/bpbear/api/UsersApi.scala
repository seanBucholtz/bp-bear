package app.bpbear.api

import zio.*
import zio.http.*
import zio.json.*
import app.bpbear.repo.*
import app.bpbear.domain.*
import java.util.Base64

object UsersApi {

  val routes: HttpApp[UserRepo, Throwable] =
    Http.collectZIO[Request] {

      // GET /users?email=<email>
      case req@Method.GET -> !! / "users" =>
        req.url.queryParams.get("email").flatMap(_.headOption) match {
          case Some(email) =>
            for {
              repo <- ZIO.service[UserRepo]
              user <- repo.getByEmail(email)
            } yield Response.json(user.toJson)
          case None =>
            ZIO.succeed(Response.status(Status.BadRequest).text("Missing 'email' query param"))
        }
    }
}
