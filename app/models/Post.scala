package models

import java.time.LocalDateTime

import play.api.libs.json.Json

/**
  * Created by therootop on 11/14/16.
  */
case class Post(
  id: Option[Long],
  uid: Long,
  text: String,
  createdAt: Option[LocalDateTime]
) {

  def patch(text: String): Post =
    this.copy(text = text)
}

object Post {
  implicit val postWrites = Json.writes[Post]
  implicit val postFormat = Json.format[Post]
}
