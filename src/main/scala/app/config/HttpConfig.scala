package app.config

import com.typesafe.config.Config

class DefaultHttpConfig(conf: Config) extends HttpConfig {
  val config = conf.getConfig("http")

  override def port: Int = config.getInt("port")
}
trait HttpConfig {
  def port: Int
}
