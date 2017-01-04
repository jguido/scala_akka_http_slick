package db.tools

import app.config.DbConfig
import org.flywaydb.core.Flyway

import scala.concurrent.duration.{Deadline, FiniteDuration}

class Migrator(dbConfig: DbConfig) {

  def migrateRetry(duration: FiniteDuration) = retry(duration.fromNow, None)

  private def retry(deadline: Deadline, e: Option[Exception]): Int = {
    if (deadline.isOverdue()) throw e.getOrElse(new Exception("Timed out connecting to db"))
    try {
      migrate()
    } catch {
      case e: Exception ⇒
        Thread.sleep(500)
        retry(deadline, Some(e))
    }
  }

  def migrate(): Int = {
    val flyway = new Flyway()
    println(s"DB... url:${dbConfig.url} , username:${dbConfig.username} , password:${dbConfig.password}")
    println()
    flyway.setDataSource(dbConfig.url, dbConfig.username, dbConfig.password)
    flyway.migrate()
  }
}
