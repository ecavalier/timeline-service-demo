package dao

import java.sql.{Date, Time, Timestamp}
import java.time.{LocalDate, LocalDateTime, LocalTime}
import javax.inject.Inject

import models.{Follow, FollowAction, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by therootop on 11/11/16.
  */
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with FollowComponent {
  import driver.api._

  private val Users = TableQuery[AccountsTable]
  private val Follows = TableQuery[FollowsTable]

  def list(page: Int, limit: Int): Future[Seq[User]] = db.run(Users drop((page - 1) * limit) take(limit) result)

  def findById(id: Long): Future[Option[User]] = db.run(Users.filter(_.id === id).result.headOption)

  def count(): Future[Int] = db.run(Users.map(_.id).length.result)

  def count(filter: String): Future[Int] = {
    db.run(Users.filter { user => user.name.toLowerCase like filter.toLowerCase }.length.result)
  }

  def insert(user: User): Future[User] = {
    val insertQuery = Users returning Users.map(_.id) into ((u, id) => u.copy(id = Option(id)))
    val action = insertQuery += user

    db.run(action)
  }

  def update(id: Long, user: User): Future[Unit] = {
    val userToUpdate: User = user.copy(Some(id))
    db.run(Users.filter(_.id === id).update(userToUpdate)).map(_ => ())
  }

  def patch(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Int] = {
    val query = Users.filter(_.id === id)

    val action = query.result.head.flatMap { user =>
      query.update(user.patch(email, name, password))
    }

    db.run(action)
  }

  def delete(id: Long): Future[Unit] = db.run(Users.filter(_.id === id).delete).map(_ => ())

  def getFollows(uid: Long): Future[Seq[User]] = {
    val query = for {
      f <- Follows filter(f => f.fid === uid && f.status === FollowAction.Follow)
      u <- Users if f.uid === u.id
    } yield u

    db.run(query result)
  }

  def getFollowedBy(uid: Long): Future[Seq[User]] = {
    val query = for {
      f <- Follows filter(f => f.uid === uid && f.status === FollowAction.Follow)
      u <- Users if f.fid === u.id
    } yield u

    db.run(query result)
  }

  def followUser(uid: Long, fid: Long, createdAt: LocalDateTime): Future[Follow] = {
    val action = Follows returning Follows.map(_.id) into ((f, id) => f.copy(id = Some(id))) += Follow(None, uid, fid, FollowAction.Follow, Some(createdAt))

    db.run(action)
  }

  def unfollowUser(uid: Long, fid: Long, createdAt: LocalDateTime): Future[Unit] = {
    val action = (Follows filter(f => f.uid === uid && f.fid === fid && f.status === FollowAction.Follow) delete) map(_ => ())

    db.run(action)
  }

  protected class AccountsTable(tag: Tag) extends Table[User](tag, "ACCOUNT") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL")
    def name = column[String]("NAME")
    def password = column[String]("PASSWORD")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp not null default CURRENT_TIMESTAMP"))

    def * = (id.?, email, name, password, createdAt) <> ((User.apply _).tupled, User.unapply _)
  }
}
