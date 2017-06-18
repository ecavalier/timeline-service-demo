package controllers

import com.google.inject.Inject
import daos.serializers.UserSerializer
import io.swagger.annotations._
import models.FollowAction
import org.json4s.JsonDSL._
import org.json4s.native.Serialization
import org.json4s.{Extraction, NoTypeHints, _}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import services.UserService
import util.RestAction
import util.RestAction._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by therootop on 11/11/16.
  */
@Api(value = "/user", description = "Operations about user")
class UserController @Inject()(userService: UserService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  implicit val jsonFormats = Serialization.formats(NoTypeHints) + new UserSerializer

  @ApiOperation(
    value = "Get Users",
    notes = "유저 목록 반환",
    response = classOf[models.UserResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def getUsers(@ApiParam(value = "Page Unit. Start from 1 and default is 1.") page: Int,
               @ApiParam(value = "Page Size Unit. Default is 20.") limit: Int) = RestAction.async { implicit req =>
    (for {
      users <- userService.list(page, limit)
    } yield users) map { users =>
      Ok(wrapJson( "result" -> Extraction.decompose(users) ))
    }
  }

  @ApiOperation(
    value = "Get a User",
    notes = "요청 id에 따른 유저 반환",
    response = classOf[models.UserResponse],
    httpMethod = "GET"
  )
  def getUser(@ApiParam(value = "User ID", defaultValue = "2", required = true) id: Long) = RestAction.async {
    (for {
      user <- userService.findById(id)
    } yield user) map { user =>
      user match {
        case Some(u) =>
          Ok(wrapJson( "result" -> Extraction.decompose(user) ))
        case None => NotFound
      }
    }
  }

  @ApiOperation(
    value = "Create a User",
    notes = "유저 생성",
    response = classOf[models.UserBody],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "User data", dataType = "models.UserBody", paramType = "body")
  ))
  def insertUser() = RestAction.async { implicit req =>
    val body = org.json4s.native.JsonMethods.parse(req.body.asJson.get.toString())
    // @TODO null value check
    val email = (body \ "email").extract[String]
    val name = (body \ "name").extract[String]
    val password = (body \ "password").extract[String]

    (for {
      result <- userService.insert(email, name, password)
    } yield result) map { user =>
      Ok(wrapJson {
        ("id" -> user.id)
      })
    }
  }

  @ApiOperation(
    value = "Update a User",
    notes = "요청 id에 해당되는 유저를 요청 데이터로 대치",
    response = classOf[models.UserBody],
    httpMethod = "PUT"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "User data", dataType = "models.UserBody", paramType = "body")
  ))
  def updateUser(@ApiParam(value = "User ID", defaultValue = "2", required = true) id: Long) = RestAction.async { implicit req =>
    val body = org.json4s.native.JsonMethods.parse(req.body.asJson.get.toString())
    // @TODO null value check
    val email = (body \ "email").extractOpt[String]
    val name = (body \ "name").extractOpt[String]
    val password = (body \ "password").extractOpt[String]

    (email, name, password) match {
      case (Some(e), Some(n), Some(p)) =>
        (for {
          user <- userService.update(id, email, name, password)
        } yield user) map { user =>
          Ok
        }
      case _ => Future(NotAcceptable)
    }
  }

  @ApiOperation(
    value = "Partial update a User (Test Unavailable on Swagger)",
    notes = "요청 id에 해당되는 유저의 데이터를 요청된 데이터에 한해서만 대치.\n" +
      "<b>Swagger 에서 PATCH method가 작동하지 않아서 테스트할 수 없습니다.</b>",
    response = classOf[models.UserBodyOption],
    httpMethod = "PATCH",
    produces = "application/json",
    consumes = "application/json"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "User data", dataType = "models.UserBodyOption", paramType = "body")
  ))
  def patchUser(@ApiParam(value = "User ID", defaultValue = "2", required = true) id: Long) = RestAction.async { implicit req =>
    val body = org.json4s.native.JsonMethods.parse(req.body.asJson.get.toString())
    val email = (body \ "email").extractOpt[String]
    val name = (body \ "name").extractOpt[String]
    val password = (body \ "password").extractOpt[String]

    (for {
      user <- userService.patch(id, email, name, password)
    } yield user) map { user =>
      Ok
    }
  }

  @ApiOperation(
    value = "Delete a User",
    notes = "요청 id에 해당하는 유저를 삭제. DB에서 삭제됨",
    httpMethod = "DELETE"
  )
  def deleteUser(@ApiParam(value = "User ID", defaultValue = "2", required = true) id: Long) = RestAction.async { implicit req =>
    // @TODO 410 Gone - Resource already deleted
    (for {
      _ <- userService.delete(id)
    } yield ()) map { _ =>
      NoContent
    }
  }

  @ApiOperation(
    value = "Get the list of users this user follows.",
    notes = "요청 id에 해당하는 유저가 팔로우 하는 유저 목록 반환.",
    response = classOf[models.UserResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def getFollows(@ApiParam(value = "User ID", defaultValue = "2", required = true) uid: Long) = RestAction.async {
    (for {
      fs <- userService.getFollows(uid)
    } yield fs) map { fs =>
      Ok(wrapJson {
        "result" -> Extraction.decompose(fs)
      })
    }
  }

  @ApiOperation(
    value = "Get the list of users this user is followed by.",
    notes = "요청 id에 해당하는 유저를 팔로우 하는 유저 목록 반환.",
    response = classOf[models.UserResponse],
    responseContainer = "List",
    httpMethod = "GET"
  )
  def getFollowedBy(@ApiParam(value = "User ID", defaultValue = "2", required = true) uid: Long) = RestAction.async {
    (for {
      fs <- userService.getFollowedBy(uid)
    } yield fs) map { fs =>
      Ok(wrapJson {
        "result" -> Extraction.decompose(fs)
      })
    }
  }

  @ApiOperation(
    value = "Make a relationship between users",
    notes = "/{uid}/follow/{fid} means that uid follow fid",
    response = classOf[models.FollowStatus],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "Empty body for Content-Type", dataType = "models.User", paramType = "body")
  ))
  def followUser(@ApiParam(value = "Follow를 할 User", defaultValue = "2", required = true) uid: Long,
                 @ApiParam(value = "Follow를 하는 대상", defaultValue = "1", required = true) fid: Long) = RestAction.async { implicit req =>
    // @TODO get uid from session
    (for {
      f <- userService.followUser(uid, fid)
    } yield f) map { f =>
      Ok(wrapJson {
        "result" -> Map(
          "outgoing_status" -> f.status
        )
      })
    }
  }

  @ApiOperation(
    value = "Breaking up a relationship",
    notes = "/{uid}/follow/{fid} means that uid unfollow fid",
    response = classOf[models.FollowStatus],
    httpMethod = "DELETE"
  )
  def unfollowUser(@ApiParam(value = "Unfollow를 할 User", defaultValue = "2", required = true) uid: Long,
                 @ApiParam(value = "Unfollow를 하는 대상", defaultValue = "1", required = true) fid: Long) = RestAction.async { implicit req =>
    // @TODO get uid from session
    (for {
      f <- userService.unfollowUser(uid, fid)
    } yield f) map { f =>
      Ok(wrapJson {
        "result" -> Map(
          "outgoing_status" -> FollowAction.Unfollow
        )
      })
    }
  }
}
