import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import config.AppConfig
import services.HelloService

object Application extends App with HelloService with BootstrapActor{
  implicit val system: ActorSystem = ActorSystem("micro-geoip")
  implicit val materializer = ActorMaterializer()
  private val load: Config = ConfigFactory.load("application")
  val appConfig = new AppConfig(load)

  val bindingFuture = Http().bindAndHandle(RouteResult.route2HandlerFlow(helloRoute), "0.0.0.0", appConfig.httpConfig.port)
}

trait BootstrapActor {

}