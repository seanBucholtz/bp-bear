package app.bpbear.api

import zio.*
import zio.http.*
import zio.json.*
import app.bpbear.service.AnalyticsService
import app.bpbear.domain.BloodPressureReading

object AnalyticsApi {

  // Helper case classes for JSON responses
  case class AvgResponse(systolic: Double, diastolic: Double)
  object AvgResponse {
    implicit val codec: JsonCodec[AvgResponse] = DeriveJsonCodec.gen[AvgResponse]
  }

  case class MinMaxResponse(min: BloodPressureReading, max: BloodPressureReading)
  object MinMaxResponse {
    implicit val codec: JsonCodec[MinMaxResponse] = DeriveJsonCodec.gen[MinMaxResponse]
  }

  case class TrendResponse(trend: String)
  object TrendResponse {
    implicit val codec: JsonCodec[TrendResponse] = DeriveJsonCodec.gen[TrendResponse]
  }

  val routes: HttpApp[AnalyticsService, Throwable] =
    Http.collectZIO[Request] {

      // GET /analytics/:userId/avg
      case Method.GET -> !! / "analytics" / userIdStr / "avg" =>
        for {
          userId <- ZIO.attempt(userIdStr.toLong)
          service <- ZIO.service[AnalyticsService]
          avgSystolic <- service.avgSystolic(userId)
          avgDiastolic <- service.avgDiastolic(userId)
        } yield Response.json(AvgResponse(avgSystolic, avgDiastolic).toJson)

      // GET /analytics/:userId/minmax
      case Method.GET -> !! / "analytics" / userIdStr / "minmax" =>
        for {
          userId <- ZIO.attempt(userIdStr.toLong)
          service <- ZIO.service[AnalyticsService]
          (min, max) <- service.minMaxReadings(userId)
        } yield Response.json(MinMaxResponse(min, max).toJson)

      // GET /analytics/:userId/trend
      case Method.GET -> !! / "analytics" / userIdStr / "trend" =>
        for {
          userId <- ZIO.attempt(userIdStr.toLong)
          service <- ZIO.service[AnalyticsService]
          trend <- service.systolicTrend(userId)
        } yield Response.json(TrendResponse(trend).toJson)
    }
}
