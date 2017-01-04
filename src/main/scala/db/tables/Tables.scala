package db.tables

import app.config.DbConfig
import db.models.{CountryId, DbCountry}

object Tables extends {
  protected val profile = slick.driver.MySQLDriver
} with Tables

trait DataConnection {
  def dbConfig: DbConfig

  protected lazy val profile = dbConfig.slickDriver

  import profile.api._
  protected lazy val db = Database.forURL(dbConfig.url, user = dbConfig.username, password = dbConfig.password, driver = dbConfig.jdbcDriver)
}

trait Tables {
  protected val profile: slick.driver.JdbcProfile

  import profile.api._
  class Countries(tag: Tag) extends Table[DbCountry](tag, "COUNTRY") {
    val id = column[CountryId]("ID", O.PrimaryKey, O.Length(36, false))
    val name = column[String]("NAME", O.Length(250, true))
    val tax = column[Double]("TAXE")
    val currency = column[String]("CURRENCY", O.Length(250, true))
    val shortCurrency = column[String]("SHORTCURRENCY", O.Length(250, true))
    val label = column[Option[String]]("LABEL", O.Length(150, true))

    def * = (id, name, tax, currency, shortCurrency, label) <> (DbCountry.tupled, DbCountry.unapply)
  }

  protected lazy val countries = new TableQuery(tag ⇒ new Countries(tag))
}