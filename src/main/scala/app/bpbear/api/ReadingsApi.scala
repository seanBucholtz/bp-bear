//package app.bpbear.api
//
//import zio._
//import zio.http._
//import zio.json._
//import app.bpbear.repo._
//import app.bpbear.domain._
//import zio.http.endpoint.openapi.OpenAPI.SecurityScheme.Http
//
//object ReadingsApi {
//
//  val routes: HttpApp[ReadingRepo] =
//    Http.collectZIO[Request] {
//      case req@Method.GET -> Root / "readings" / userIdStr =>
//        for {
//          userId <- ZIO.attempt(userIdStr.toLong)
//          repo <- ZIO.service[ReadingRepo]
//          readings <- repo.getByUser(userId)
//        } yield Response.json(readings.toJson)
//    }
//}
