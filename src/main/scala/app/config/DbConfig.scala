package app.config

import com.typesafe.config

import slick.driver._

class DefaultDbConfig(conf: config.Config) extends DbConfig {
  val dbConf = conf.getConfig("db")

  override def url: String = dbConf.getString("url")

  override def username: String = dbConf.getString("username")

  override def password: String = dbConf.getString("password")

  override def jdbcDriver: String = dbConf.getString("jdbcDriver")

  override def slickDriver: JdbcDriver = {
    import scala.reflect.runtime.universe
    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

    val module = runtimeMirror.staticModule(dbConf.getString("slickDriver") + "$")

    val obj = runtimeMirror.reflectModule(module)
    obj.instance.asInstanceOf[JdbcDriver]
  }
}


trait DbConfig {
  def url: String

  def jdbcDriver: String

  def slickDriver: JdbcDriver

  def username: String

  def password: String
}
