package models

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties}
import play.api.libs.json.Json
import play.api.libs.json._

/**
  * Created by jinwookim on 11/11/16.
  */
case class User(
  id: Option[Long],
  email: String,
  name: String,
  password: String,
  createdAt: Option[LocalDateTime]
) {

  def patch(email: Option[String], name: Option[String], password: Option[String]): User =
    this.copy(email = email.getOrElse(this.email),
              name = name.getOrElse(this.name),
              password = password.getOrElse(this.password)
    )
}

object User {
  implicit val userWrites = Json.writes[User]
  implicit val userFormat = Json.format[User]
}
