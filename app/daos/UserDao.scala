package daos

import java.time.LocalDateTime

import com.google.inject.ImplementedBy
import daos.Impl.UserDaoImpl
import models.{Follow, User}

import scala.concurrent.Future

/**
  * Created by therootop on 11/11/16.
  */
@ImplementedBy(classOf[UserDaoImpl])
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
