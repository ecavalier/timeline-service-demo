package services

import com.google.inject.ImplementedBy
import models.Post
import services.Impl.PostServiceImpl

import scala.concurrent.Future

/**
  * Created by therootop on 11/14/16.
  */
@ImplementedBy(classOf[PostServiceImpl])
trait PostService {

  def list(page: Int, limit: Int): Future[Seq[Post]]

  def findById(id: Long): Future[Option[Post]]

  def findByUid(uid: Long): Future[Vector[Post]]

  def count(): Future[Int]

  def insert(uid: Long, text: String): Future[Post]

  def update(id: Long, text: String): Future[Int]

  def delete(id: Long): Future[Unit]

  def timeline(id: Long, page: Int, limit: Int): Future[Seq[Post]]

}
