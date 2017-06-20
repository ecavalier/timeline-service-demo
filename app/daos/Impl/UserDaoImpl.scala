package daos.Impl

import java.time.LocalDateTime
import javax.inject.Inject

import daos.UserDao
import daos.tables.{FollowsTable, SlickRepository, UserTable}
import models.{Follow, FollowAction, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by rootop on 2017-06-18.
  */
class UserDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with UserDao with UserTable with SlickRepository with FollowsTable {

  import profile.api._

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

}

