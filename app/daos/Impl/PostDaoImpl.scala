package daos.Impl

import javax.inject.Inject

import daos.PostDao
import daos.tables.{FollowsTable, PostTable, SlickRepository}
import models.Post
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by therootop on 2017-06-18.
  */
class PostDaoImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with PostDao with FollowsTable with SlickRepository with PostTable {

  import profile.api._

  def list(page: Int, limit: Int): Future[Seq[Post]] = db.run(Posts drop((page - 1) * limit) take(limit) result)

  def findById(id: Long): Future[Option[Post]] = db.run(Posts.filter(_.id === id).result.headOption)

  def findByUid(uid: Long): Future[Vector[Post]] = db.run(Posts.filter(_.uid === uid).sortBy(_.id desc).to[Vector].result)

  def count(): Future[Int] = db.run(Posts.map(_.id).length result)

  def insert(post: Post): Future[Post] = {
    val insertQuery = Posts returning Posts.map(_.id) into ((p, id) => p.copy(id = Option(id)))
    val action = insertQuery += post

    db.run(action)
  }

  def update(id: Long, text: String): Future[Int] = {
    val query = Posts filter(_.id === id)
    val action = query.result.head.flatMap { post =>
      query.update(post.patch(text))
    }

    db.run(action)
  }

  def delete(id: Long): Future[Unit] = db.run(Posts filter(_.id === id) delete) map(_ => ())

  def timeline(id: Long, page: Int, limit: Int): Future[Seq[Post]] = {
    val followsQuery = Follows filter(_.fid === id) map(_.uid)
    val followerPosts = Posts.filter(_.uid in followsQuery)
    val selfPosts = Posts.filter(_.uid === id)

    val action = followerPosts union selfPosts sortBy (p => (p.createdAt desc, p.id desc)) drop((page - 1) * limit) take(limit)

    db.run(action result)
  }

}

