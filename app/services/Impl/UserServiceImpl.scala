package services.Impl

import java.time.LocalDateTime

import com.google.inject.Inject
import daos.UserDAO
import models.{Follow, User}
import org.mindrot.jbcrypt.BCrypt
import services.UserService

import scala.concurrent.Future

/**
  * Created by therootop on 2017-06-18.
  */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  def list(page: Int, limit: Int): Future[Seq[User]] = {
    userDAO.list(page, limit)
  }

  def findById(id: Long): Future[Option[User]] = {
    userDAO.findById(id)
  }

  def count(): Future[Int] = {
    userDAO.count()
  }

  def count(filter: String) = {
    userDAO.count(filter)
  }

  def insert(user: User): Future[User] = {
    userDAO.insert(user)
  }

  def insert(email: String, name: String, password: String): Future[User] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    val user = User(None, email, name, hash(password), Some(createdAt))
    userDAO.insert(user)
  }

  def update(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Unit] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    val user = User(None, email.get, name.get, hash(password.get), Some(createdAt))
    userDAO.update(id, user)
  }

  def patch(id: Long, email: Option[String], name: Option[String], password: Option[String]): Future[Int] = {
    val hashed = password match {
      case Some(p) => Some(hash(p))
      case None => password
    }

    userDAO.patch(id, email, name, hashed)
  }

  def delete(id: Long): Future[Unit] = {
    userDAO.delete(id)
  }

  def getFollows(uid: Long): Future[Seq[User]] = {
    userDAO.getFollows(uid)
  }

  def getFollowedBy(uid: Long): Future[Seq[User]] = {
    userDAO.getFollowedBy(uid)
  }

  def followUser(uid: Long, fid: Long): Future[Follow] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    userDAO.followUser(uid, fid, createdAt)
  }

  def unfollowUser(uid: Long, fid: Long): Future[Unit] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    userDAO.unfollowUser(uid, fid, createdAt)
  }

  private val salt: String = BCrypt.gensalt()

  private def hash(v: String): String = BCrypt.hashpw(v, salt)

  private def compare(v: String, hashed: String): Boolean = hash(v) == hashed
}

