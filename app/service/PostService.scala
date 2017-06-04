package service

import java.time.LocalDateTime
import javax.inject.Inject

import dao.PostDao
import models.Post

import scala.concurrent.Future

/**
  * Created by jinwookim on 11/14/16.
  */
class PostService @Inject()(postDAO: PostDao) {

  def list(page: Int, limit: Int): Future[Seq[Post]] = {
    postDAO.list(page, limit)
  }

  def findById(id: Long): Future[Option[Post]] = {
    postDAO.findById(id)
  }

  def findByUid(uid: Long): Future[Vector[Post]] = {
    postDAO.findByUid(uid)
  }

  def count(): Future[Int] = {
    postDAO.count()
  }

  def insert(uid: Long, text: String): Future[Post] = {
    // @TODO using DB default value
    val createdAt = LocalDateTime.now
    val post = Post(None, uid, text, Some(createdAt))
    postDAO.insert(post)
  }

  def update(id: Long, text: String): Future[Int] = {
    postDAO.update(id, text)
  }

  def delete(id: Long): Future[Unit] = {
    postDAO.delete(id)
  }

  def timeline(id: Long, page: Int, limit: Int): Future[Seq[Post]] = {
    postDAO.timeline(id, page, limit)
  }
}
