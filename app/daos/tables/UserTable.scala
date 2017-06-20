package daos.tables

import java.time.LocalDateTime

import models.User
import slick.sql.SqlProfile.ColumnOption.SqlType

/**
  * Created by rootop on 2017-06-18.
  */
trait UserTable extends ImplicitTypeMapper {

  this: SlickRepository => // has to be mixed in with SlickRepository

  import drivers.api._

  implicit val _localDateTimeToDate = localDateTimeToDate

  protected class AccountsTable(tag: Tag) extends Table[User](tag, "ACCOUNT") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL")
    def name = column[String]("NAME")
    def password = column[String]("PASSWORD")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp not null default CURRENT_TIMESTAMP"))

    def * = (id.?, email, name, password, createdAt) <> ((User.apply _).tupled, User.unapply _)
  }

  protected val Users = TableQuery[AccountsTable]
}
