package util

import org.json4s.JsonDSL.WithBigDecimal._
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

/**
  * Created by therootop on 11/11/16.
  */
object RestAction extends ActionBuilder[Request] {
  implicit val jsonFormats = Serialization.formats(NoTypeHints)

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val origin = request.headers.get("Origin").getOrElse("*")

    request.acceptedTypes.head.toString() match {
      case "application/json" => block(request).map(_.withHeaders(
        "Access-Control-Allow-Origin" -> origin,
        "Access-Control-Allow-Credentials" -> "true",
        "Content-Type" -> "application/json"
      ))

      case "text/html" => block(request)

      case _ => block(request).map(_.withHeaders(
        "Access-Control-Allow-Origin" -> origin,
        "Access-Control-Allow-Credentials" -> "true"
      ))
    }
  }

  def wrapJson(action: => JObject): String = {
    try {
      write(action ~ ("status" -> "success"))

    } catch {
      case e: Throwable => {
        Logger.error("Error while performing action", e)

        write {
          ("status" -> "failed") ~
            ("message" -> e.getMessage)
        }
      }
    }
  }
}
