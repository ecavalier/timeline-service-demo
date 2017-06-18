package daos

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
trait UserDao {

  def list(page: Int, limit: Int): Future[Seq[User]]

  def findById(id: Long): Future[Option[User]]

  def count(): Future[Int]

  def count(filter: String): Future[Int]

  def insert(user: User): Future[User]

  def update(id: Long, user: User): Future[Unit]

  def patch(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Int]

  def delete(id: Long): Future[Unit]

  def getFollows(uid: Long): Future[Seq[User]]

  def getFollowedBy(uid: Long): Future[Seq[User]]

  def followUser(uid: Long, fid: Long, createdAt: LocalDateTime): Future[Follow]

  def unfollowUser(uid: Long, fid: Long, createdAt: LocalDateTime): Future[Unit]

}
