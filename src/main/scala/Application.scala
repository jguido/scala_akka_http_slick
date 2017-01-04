import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult
import akka.stream.ActorMaterializer
import app.config.AppConfig
import com.typesafe.config.{Config, ConfigFactory}
import db.DatabaseSupport
import http.services.HelloService

object Application extends App with HelloService with BootstrapActor with DatabaseSupport{
  implicit val system: ActorSystem = ActorSystem("micro-geoip")
  implicit val materializer = ActorMaterializer()
  private val load: Config = ConfigFactory.load("application")
  val appConfig = new AppConfig(load)

  DatabaseSupport.applyMigrate(appConfig.dbConfig)

  val bindingFuture = Http().bindAndHandle(RouteResult.route2HandlerFlow(helloRoute), "0.0.0.0", appConfig.httpConfig.port)
}

trait BootstrapActor {

}