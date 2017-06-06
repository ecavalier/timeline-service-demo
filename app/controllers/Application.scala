package controllers

import org.h2.tools.Server
import play.api.mvc._

class Application extends Controller {

  def index = Action {
//    Server.createWebServer("-webAllowOthers","-webPort","8082").start()
//    Server.createTcpServer("-tcpAllowOthers","-tcpPort","9092").start()

    // @TODO Route to /public/dist
    // @TODO use webjars
    Ok(views.html.swagger())
  }

  def preflight(all: String) = Action {
    Ok("").withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent"
    )
  }

}