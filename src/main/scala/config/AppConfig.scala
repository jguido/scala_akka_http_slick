package config

import com.typesafe.config.Config

class AppConfig(load: Config) extends BaseConfig{

  override val httpConfig: HttpConfig = new DefaultHttpConfig(load.getConfig("app"))
}
trait BaseConfig {
  val httpConfig: HttpConfig
}

class DefaultHttpConfig(conf: Config) extends HttpConfig {
  val config = conf.getConfig("http")

  override def port: Int = config.getInt("port")
}

trait HttpConfig {
  def port: Int
}