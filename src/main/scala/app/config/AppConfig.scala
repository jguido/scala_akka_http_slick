package app.config

import com.typesafe.config.Config

class AppConfig(load: Config) extends BaseConfig{
  private val config: Config = load.getConfig("app")

  override val httpConfig: HttpConfig = new DefaultHttpConfig(config)
  override val dbConfig: DbConfig = new DefaultDbConfig(config)
}

trait BaseConfig {
  val httpConfig: HttpConfig
  val dbConfig: DbConfig
}





