package daos

import java.time.LocalDateTime
import javax.inject.Inject

import com.google.inject.ImplementedBy
import daos.Impl.PostDaoImpl
import models.Post
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by therootop on 11/14/16.
  */
@ImplementedBy(classOf[PostDaoImpl])
trait PostDao {

  def list(page: Int, limit: Int): Future[Seq[Post]]

  def findById(id: Long): Future[Option[Post]]

  def findByUid(uid: Long): Future[Vector[Post]]

  def count(): Future[Int]

  def insert(post: Post): Future[Post]

  def update(id: Long, text: String): Future[Int]

  def delete(id: Long): Future[Unit]

  def timeline(id: Long, page: Int, limit: Int): Future[Seq[Post]]

}
