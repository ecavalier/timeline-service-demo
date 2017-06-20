package daos

import com.google.inject.ImplementedBy
import daos.Impl.PostDaoImpl
import models.Post

import scala.concurrent.Future

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
