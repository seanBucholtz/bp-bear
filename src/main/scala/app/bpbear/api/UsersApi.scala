//package app.bpbear.api
//
//import zio._
//import zio.http._
//import zio.json._
//import app.bpbear.repo._
//import app.bpbear.domain._
//import java.util.Base64
//
//object UsersApi {
//
//  val routes: HttpApp[UserRepo] =
//    Http.collectZIO[Request] {
//
//      // GET /users?email=<email>
//      case req@Method.GET -> Root / "users" =>
//        req.url.queryParams.get("email").flatMap(_.headOption) match {
//          case Some(email) =>
//            for {
//              repo <- ZIO.service[UserRepo]
//              user <- repo.getByEmail(email)
//            } yield Response.json(user.toJson)
//          case None =>
//            ZIO.succeed(Response.status(Status.BadRequest).text("Missing 'email' query param"))
//        }
//    }
//}
