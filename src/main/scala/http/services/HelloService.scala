package http.services

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import app.config.Loggable
import ch.megard.akka.http.cors.CorsDirectives._
import http.protocol.{Hello, HelloFormats}

trait HelloService extends BaseService with  HelloFormats with Loggable{

  def helloRoute = cors(settings) {
    path("hello") {
      get {
        parameter('name.?) { name =>
          complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(helloFormat.write(Hello(name.getOrElse("You"))).toString)))
        }
      }
    }
  }
}
