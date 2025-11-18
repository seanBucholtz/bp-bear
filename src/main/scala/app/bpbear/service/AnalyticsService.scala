package app.bpbear.service

import zio.*
import app.bpbear.repo.ReadingRepo
import app.bpbear.domain.BloodPressureReading
import java.time.Instant

trait AnalyticsService {

  /** Average systolic value for a user */
  def avgSystolic(userId: Long): Task[Double]

  /** Average diastolic value for a user */
  def avgDiastolic(userId: Long): Task[Double]

  /** Min/max readings for a user */
  def minMaxReadings(userId: Long): Task[(BloodPressureReading, BloodPressureReading)]

  /** Trend detection: true if readings are generally increasing */
  def systolicTrend(userId: Long): Task[String]
}

object AnalyticsService {

  final case class Live(readingRepo: ReadingRepo) extends AnalyticsService {

    override def avgSystolic(userId: Long): Task[Double] =
      for {
        readings <- readingRepo.getByUser(userId)
        avg = if (readings.nonEmpty) readings.map(_.systolic).sum.toDouble / readings.size else 0.0
      } yield avg

    override def avgDiastolic(userId: Long): Task[Double] =
      for {
        readings <- readingRepo.getByUser(userId)
        avg = if (readings.nonEmpty) readings.map(_.diastolic).sum.toDouble / readings.size else 0.0
      } yield avg

    override def minMaxReadings(userId: Long): Task[(BloodPressureReading, BloodPressureReading)] =
      for {
        readings <- readingRepo.getByUser(userId)
        sorted = readings.sortBy(_.recordedAt.toEpochMilli)
        min = sorted.headOption.getOrElse(
          BloodPressureReading(0, userId, 0, 0, 0, Instant.now())
        )
        max = sorted.lastOption.getOrElse(min)
      } yield (min, max)

    override def systolicTrend(userId: Long): Task[String] =
      for {
        readings <- readingRepo.getByUser(userId)
        trend = if (readings.size < 2) "stable"
        else if (readings.last.systolic > readings.head.systolic) "increasing"
        else if (readings.last.systolic < readings.head.systolic) "decreasing"
        else "stable"
      } yield trend
  }

  val live: ZLayer[ReadingRepo, Nothing, AnalyticsService] =
    ZLayer.fromFunction(Live.apply)
}
