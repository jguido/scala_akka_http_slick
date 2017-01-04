package app.config

import com.typesafe.config.Config

class AppConfig(load: Config) extends BaseConfig{

  override val httpConfig: HttpConfig = new DefaultHttpConfig(load.getConfig("app"))
  override val dbConfig: DbConfig = new DefaultDbConfig(load.getConfig("app"))
}

trait BaseConfig {
  val httpConfig: HttpConfig
  val dbConfig: DbConfig
}





