package dao

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import models.Follow
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.Tag
import slick.profile.SqlProfile.ColumnOption.SqlType

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.H2Driver.api._
import play.api.db.slick._

/**
  * Created by jinwookim on 11/15/16.
  */
class FollowDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

}

trait FollowComponent {
  implicit val localDateTimeToDate = MappedColumnType.base[LocalDateTime, Timestamp](
    l => Timestamp.valueOf(l),
    d => d.toLocalDateTime
  )

  class FollowsTable(tag: Tag) extends Table[Follow](tag, "FOLLOW") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Long]("USER_ID")
    def fid = column[Long]("FOLLOWER_ID")
    def status = column[String]("STATUS")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp not null default CURRENT_TIMESTAMP"))

    def * = (id.?, uid, fid, status, createdAt) <> ((Follow.apply _).tupled, Follow.unapply)
  }
}