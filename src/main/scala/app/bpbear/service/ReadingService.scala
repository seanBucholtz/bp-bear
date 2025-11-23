//package app.bpbear.service
//
//import zio._
//import app.bpbear.repo._
//import app.bpbear.domain._
//
//trait ReadingService {
//  def addReading(reading: BloodPressureReading): Task[Long]
//
//  def getUserReadings(userId: Long): Task[List[BloodPressureReading]]
//}
//
//object ReadingService {
//
//  final case class Live(repo: ReadingRepo) extends ReadingService {
//    override def addReading(reading: BloodPressureReading): Task[Long] =
//      repo.insert(reading)
//
//    override def getUserReadings(userId: Long): Task[List[BloodPressureReading]] =
//      repo.getByUser(userId)
//  }
//
//  val live: ZLayer[ReadingRepo, Nothing, ReadingService] =
//    ZLayer.fromFunction(Live.apply)
//}
