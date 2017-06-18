//import javax.inject.Inject
//
//import akka.stream.Materializer
//import play.api.{Application, GlobalSettings, Play}
//import play.api.mvc.{EssentialAction, Handler, RequestHeader, WithFilters}
//import play.filters.gzip.GzipFilter
//
//import scala.concurrent.ExecutionContext.Implicits.global
//
///**
//  * Created by therootop on 11/16/16.
//  */
//class Global @Inject() (implicit val mat: Materializer) {
//
//  def AccessControlAllowOrigin(action: EssentialAction): EssentialAction = EssentialAction { request =>
//    val origin = request.headers.get("Origin").getOrElse("*")
//    request.acceptedTypes.head.toString() match {
//      case "application/json" => action(request).map(_.withHeaders(
//        "Access-Control-Allow-Origin" -> origin,
//        "Access-Control-Allow-Credentials" -> "true",
//        "Content-Type" -> "application/json"
//      ))
//      case "text/html" => action(request)
//      case _ => action(request).map(_.withHeaders(
//        "Access-Control-Allow-Origin" -> origin,
//        "Access-Control-Allow-Credentials" -> "true"
//      ))
//    }
//  }
//
//  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
//    super.onRouteRequest(request).map { handler =>
//      handler match {
//        case action: EssentialAction => AccessControlAllowOrigin(action)
//        case other => other
//      }
//    }
//  }
//}