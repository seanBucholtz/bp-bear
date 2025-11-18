package app.bpbear.repo

import app.bpbear.domain.*
import doobie.*
import doobie.implicits.*
import zio.*
import doobie.util.transactor.Transactor

trait ReadingRepo {
  def insert(reading: BloodPressureReading): Task[Long]

  def getByUser(userId: Long): Task[List[BloodPressureReading]]
}

object ReadingRepo {

  final case class Live(xa: Transactor[Task]) extends ReadingRepo {

    override def insert(reading: BloodPressureReading): Task[Long] =
      sql"""
        INSERT INTO readings (user_id, systolic, diastolic, pulse, recorded_at)
        VALUES (
          ${reading.userId},
          ${reading.systolic},
          ${reading.diastolic},
          ${reading.pulse},
          ${reading.recordedAt}
        )
      """.update
        .withUniqueGeneratedKeys[Long]("id")
        .transact(xa)

    override def getByUser(userId: Long): Task[List[BloodPressureReading]] =
      sql"""
        SELECT id, user_id, systolic, diastolic, pulse, recorded_at
        FROM readings
        WHERE user_id = $userId
      """
        .query[BloodPressureReading]
        .to[List]
        .transact(xa)
  }

  val live: ZLayer[Transactor[Task], Nothing, ReadingRepo] =
    ZLayer.fromFunction(Live.apply)
}
