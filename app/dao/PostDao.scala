package dao

import java.time.LocalDateTime
import javax.inject.Inject

import models.Post
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by therootop on 11/14/16.
  */
class PostDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with FollowComponent {
  import driver.api._

  private val Posts = TableQuery[PostsTable]
  private val Follows = TableQuery[FollowsTable]

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

  protected class PostsTable(tag: Tag) extends Table[Post](tag, "POST") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Long]("USER_ID")
    def text = column[String]("TEXT")
    def createdAt = column[Option[LocalDateTime]]("CREATED_AT", SqlType("timestamp not null default CURRENT_TIMESTAMP"))

    def * = (id.?, uid, text, createdAt) <> ((Post.apply _).tupled, Post.unapply)
  }

}
