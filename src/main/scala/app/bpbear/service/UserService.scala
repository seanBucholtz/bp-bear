//package app.bpbear.service
//
//import zio._
//import app.bpbear.repo._
//import app.bpbear.domain._
//import org.mindrot.jbcrypt.BCrypt
//
//trait UserService {
//  def register(user: User, password: String): Task[Long]
//
//  def findByEmail(email: String): Task[Option[User]]
//}
//
//object UserService {
//
//  final case class Live(repo: UserRepo) extends UserService {
//
//    override def register(user: User, password: String): Task[Long] =
//      for {
//        hashed <- ZIO.attempt(BCrypt.hashpw(password, BCrypt.gensalt()))
//        newUser = user.copy(hashedPassword = hashed)
//        id <- repo.insert(newUser)
//      } yield id
//
//    override def findByEmail(email: String): Task[Option[User]] =
//      repo.getByEmail(email)
//  }
//
//  val live: ZLayer[UserRepo, Nothing, UserService] =
//    ZLayer.fromFunction(Live.apply)
//}
