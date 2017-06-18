package services.Impl

import java.time.LocalDateTime
import javax.inject.Inject

import daos.PostDao
import models.Post
import services.PostService

import scala.concurrent.Future

/**
  * Created by therootop on 2017-06-18.
  */
class PostServiceImpl @Inject() (postDAO: PostDao) extends PostService {

  override def list(page: Int, limit: Int): Future[Seq[Post]] = {
    postDAO.list(page, limit)
  }

  override def findById(id: Long): Future[Option[Post]] = {
    postDAO.findById(id)
  }

  override def findByUid(uid: Long): Future[Vector[Post]] = {
    postDAO.findByUid(uid)
  }

  override def count(): Future[Int] = {
    postDAO.count()
  }

  override def insert(uid: Long, text: String): Future[Post] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    val post = Post(None, uid, text, Some(createdAt))
    postDAO.insert(post)
  }

  override def update(id: Long, text: String): Future[Int] = {
    postDAO.update(id, text)
  }

  override def delete(id: Long): Future[Unit] = {
    postDAO.delete(id)
  }

  override def timeline(id: Long, page: Int, limit: Int): Future[Seq[Post]] = {
    postDAO.timeline(id, page, limit)
  }
}
