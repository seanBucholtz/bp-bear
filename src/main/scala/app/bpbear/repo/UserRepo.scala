//package app.bpbear.repo
//
//import app.bpbear.domain.User
//import doobie._
//import doobie.implicits._
//import zio._
//import doobie.util.transactor.Transactor
//import org.mindrot.jbcrypt.BCrypt
//
//trait UserRepo {
//  def insert(user: User): Task[Long]
//
//  def getByEmail(email: String): Task[Option[User]]
//}
//
//object UserRepo {
//
//  final case class Live(xa: Transactor[Task]) extends UserRepo {
//
//    override def insert(user: User): Task[Long] =
//      sql"""
//        INSERT INTO users (email, hashed_password)
//        VALUES (${user.email}, ${user.hashedPassword})
//      """.update
//        .withUniqueGeneratedKeys[Long]("id")
//        .transact(xa)
//
//    override def getByEmail(email: String): Task[Option[User]] =
//      sql"""
//        SELECT id, email, hashed_password
//        FROM users
//        WHERE email = $email
//      """.query[User].option
//        .transact(xa)
//  }
//
//  val live: ZLayer[Transactor[Task], Nothing, UserRepo] =
//    ZLayer.fromFunction(Live.apply)
//}
