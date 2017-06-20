package daos.tables

import java.sql.Timestamp
import java.time.LocalDateTime

import models.Post
import slick.sql.SqlProfile.ColumnOption.SqlType

/**
  * Created by rootop on 2017-06-19.
  */
trait PostTable extends ImplicitTypeMapper {

  this: SlickRepository => // has to be mixed in with SlickRepository

  import drivers.api._

  implicit val _optionLocalDateTimeToDate = optionLocalDateTimeToDate

  protected class PostsTable(tag: Tag) extends Table[Post](tag, "POST") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Long]("USER_ID")
    def text = column[String]("TEXT")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp default CURRENT_TIMESTAMP"))

    def * = (id.?, uid, text, createdAt) <> ((Post.apply _).tupled, Post.unapply _)
  }

  protected val Posts = TableQuery[PostsTable]
}
