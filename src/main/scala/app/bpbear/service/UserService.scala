package app.bpbear.service

import zio.*
import app.bpbear.repo.*
import app.bpbear.domain.*
import zio.crypto.BCrypt

trait UserService {
  def register(user: User, password: String): Task[Long]

  def findByEmail(email: String): Task[Option[User]]
}

object UserService {

  final case class Live(repo: UserRepo) extends UserService {

    override def register(user: User, password: String): Task[Long] =
      for {
        hashed <- ZIO.attempt(BCrypt.hashpw(password, BCrypt.gensalt()))
        newUser = user.copy(hashedPassword = hashed)
        id <- repo.insert(newUser)
      } yield id

    override def findByEmail(email: String): Task[Option[User]] =
      repo.getByEmail(email)
  }

  val live: ZLayer[UserRepo, Nothing, UserService] =
    ZLayer.fromFunction(Live.apply)
}
