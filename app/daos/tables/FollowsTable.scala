package daos.tables

import java.time.LocalDateTime

import models.Follow
import slick.sql.SqlProfile.ColumnOption.SqlType

/**
  * Created by rootop on 2017-06-18.
  */
trait FollowsTable extends ImplicitTypeMapper {

  this: SlickRepository => // has to be mixed in with SlickRepository

  import drivers.api._

  implicit val _localDateTimeToTimestamp = localDateTimeToDate

  class FollowsTable(tag: Tag) extends Table[Follow](tag, "FOLLOW") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Long]("USER_ID")
    def fid = column[Long]("FOLLOWER_ID")
    def status = column[String]("STATUS")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp not null default CURRENT_TIMESTAMP"))

    def * = (id.?, uid, fid, status, createdAt) <> ((Follow.apply _).tupled, Follow.unapply _)
  }

  protected val Follows = TableQuery[FollowsTable]
}
