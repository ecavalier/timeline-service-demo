package controllers

import javax.inject.Inject

import daos.serializers.PostSerializer
import io.swagger.annotations._
import models.Post
import org.json4s.JsonDSL._
import org.json4s.{Extraction, NoTypeHints}
import org.json4s.native.Serialization
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import services.PostService
import util.RestAction
import util.RestAction._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by therootop on 11/14/16.
  */
@Api(value = "/posts", description = "Operation about Post")
class PostController @Inject()(postService: PostService, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  implicit val jsonFormats = Serialization.formats(NoTypeHints) + new PostSerializer

  @ApiOperation(
    value = "Get Posts",
    notes = "Post 목록 반환",
    response = classOf[models.PostResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def getPosts(@ApiParam(value = "Page Unit. Start from 1 and default is 1.") page: Int,
               @ApiParam(value = "Page Size Unit. Default is 20.") limit: Int) = RestAction.async {
    (for {
      posts <- postService.list(page, limit)
    } yield posts) map { posts =>
      Ok(wrapJson { "result" -> Extraction.decompose(posts) })
    }
  }

  @ApiOperation(
    value = "Get Post By ID",
    notes = "요청 id에 해당하는 Post 반환",
    response = classOf[models.PostResponse],
    httpMethod = "GET"
  )
  def getPost(@ApiParam(value = "Post ID", defaultValue = "1", required = true) id: Long) = RestAction.async {
    (for {
      post <- postService.findById(id)
    } yield post) map { post =>
      post match {
        case Some(p) =>
          Ok(wrapJson {
            "result" -> Extraction.decompose(post)
          })
        case None =>
          NotFound
      }
    }
  }

  @ApiOperation(
    value = "Get Post By User ID",
    notes = "요청 User id가 작성한 Post 목록 반환",
    response = classOf[models.PostResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def getPostsByUid(@ApiParam(value = "User ID", defaultValue = "2", required = true) uid: Long) = RestAction.async {
    (for {
      post <- postService.findByUid(uid)
    } yield post) map { post =>
      Ok(wrapJson {
        "result" -> Extraction.decompose(post.sortBy(_.id.get).reverse)
      })
    }
  }

  @ApiOperation(
    value = "Create a Post",
    notes = "Post 새로 생성",
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "Post data", dataType = "models.PostBody", paramType = "body")
  ))
  def insertPost() = RestAction.async { implicit req =>
    val body = org.json4s.native.JsonMethods.parse(req.body.asJson.get.toString())
    val uid = (body \ "uid").extract[Long]
    val text = (body \ "text").extract[String]

    (for {
      post <- postService.insert(uid, text)
    } yield post) map { post =>
      Ok(wrapJson {
        ("id" -> post.id)
      })
    }
  }

  @ApiOperation(
    value = "Update a Post",
    notes = "요청 id에 해당하는 Post 내용 업데이트",
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "Post data", dataType = "models.PostText", paramType = "body")
  ))
  def updatePost(@ApiParam(value = "Post ID", defaultValue = "1", required = true) id: Long) = RestAction.async { implicit req =>
    val body = org.json4s.native.JsonMethods.parse(req.body.asJson.get.toString())
    val text = (body \ "text").extractOpt[String]

    text match {
      case Some(t) =>
        (for {
          _ <- postService.update(id, t)
        } yield ()) map { _ =>
          Ok
        }
      case _ => Future(NotAcceptable)
    }
  }

  @ApiOperation(
    value = "Delete a Post",
    notes = "요청 id에 해당하는 Post 삭제",
    httpMethod = "DELETE"
  )
  def delete(@ApiParam(value = "Post ID", defaultValue = "1", required = true) id: Long) = RestAction.async {
    (for {
      _ <- postService.delete(id)
    } yield ()) map { _ =>
      NoContent
    }
  }

  @ApiOperation(
    value = "Get User's News Feed",
    notes = "요청 User id에 해당하는 뉴스 피드 ",
    response = classOf[models.PostResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def timeline(@ApiParam(value = "User ID", defaultValue = "2", required = true) id: Long,
               @ApiParam(value = "Page Unit. Start from 1 and default is 1.") page: Int,
               @ApiParam(value = "Page Size Unit. Default is 20.") limit: Int) = RestAction.async {
    (for {
      posts <- postService.timeline(id, page, limit)
    } yield posts) map { posts =>
      Ok(wrapJson {
        "result" -> Extraction.decompose(posts)
      })
    }
  }

}
