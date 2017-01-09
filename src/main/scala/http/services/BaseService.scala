package http.services

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.HttpOriginRange
import app.config.Loggable
import ch.megard.akka.http.cors.CorsSettings

trait BaseService extends Loggable {

  implicit val system: ActorSystem
  val settings = CorsSettings.defaultSettings.copy(allowedOrigins = HttpOriginRange.*, allowCredentials = false)
}
