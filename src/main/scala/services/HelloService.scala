package services

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.HttpOriginRange
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.CorsDirectives._
import ch.megard.akka.http.cors.CorsSettings
import config.Loggable
import domain.{Hello, HelloFormats}

trait HelloService extends HelloFormats with Loggable{

  implicit val system: ActorSystem
  val settings = CorsSettings.defaultSettings.copy(allowedOrigins = HttpOriginRange.*, allowCredentials = false)

  def helloRoute = cors(settings) {
    path("hello") {
      parameter('name.?) { name =>
        complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(helloFormat.write(Hello(name.getOrElse("You"))).toString)))
      }
    }
  }
}
