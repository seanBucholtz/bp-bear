package app.bpbear.domain

import zio.json.*

case class User(
                 id: Long,
                 email: String,
                 hashedPassword: String
               )

object User {
  implicit val codec: JsonCodec[User] = DeriveJsonCodec.gen[User]
}
