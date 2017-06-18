package services

import com.google.inject.ImplementedBy
import models.{Follow, User}
import services.Impl.UserServiceImpl

import scala.concurrent.Future

/**
  * Created by therootop on 11/11/16.
  */
@ImplementedBy(classOf[UserServiceImpl])
trait UserService {

  def list(page: Int, limit: Int): Future[Seq[User]]

  def findById(id: Long): Future[Option[User]]

  def count(): Future[Int]

  def count(filter: String)

  def insert(user: User): Future[User]

  def insert(email: String, name: String, password: String): Future[User]

  def update(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Unit]

  def patch(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Int]

  def delete(id: Long): Future[Unit]

  def getFollows(uid: Long): Future[Seq[User]]

  def getFollowedBy(uid: Long): Future[Seq[User]]

  def followUser(uid: Long, fid: Long): Future[Follow]

  def unfollowUser(uid: Long, fid: Long): Future[Unit]

}
