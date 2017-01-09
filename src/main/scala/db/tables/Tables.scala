package db.tables

import app.config.DbConfig
import db.models._

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
    val id            = column[CountryId]("ID", O.PrimaryKey, O.Length(36, false))
    val name          = column[String]("NAME", O.Length(250, true))
    val tax           = column[Double]("TAXE")
    val currency      = column[String]("CURRENCY", O.Length(250, true))
    val shortCurrency = column[String]("SHORT_CURRENCY", O.Length(250, true))
    val label         = column[Option[String]]("LABEL", O.Length(150, true))

    def * = (id, name, tax, currency, shortCurrency, label) <> ((DbCountry.apply _).tupled, DbCountry.unapply)
  }

  protected lazy val countries = TableQuery[Countries]

  class Users(tag: Tag) extends Table[DbUser](tag, "USERS") {
    val id         = column[UserId]("ID", O.PrimaryKey, O.Length(36, false))
    val principal  = column[String]("PRINCIPAL", O.Length(256, varying = true))
    val credential = column[Array[Byte]]("CREDENTIAL")

    def * = (id, principal, credential) <> ((DbUser.apply _).tupled, DbUser.unapply)
  }

  protected lazy val users = TableQuery[Users]

  class Tokens(tag: Tag) extends Table[DbToken](tag, "TOKENS") {
    val id  = column[TokenId]("ID", O.PrimaryKey, O.Length(36, false))
    val userId  = column[UserId]("USER_ID", O.Length(36, false))

    def * = (id, userId) <> ((DbToken.apply _).tupled, DbToken.unapply)

    def user = foreignKey("FK_SESSION_USER", userId, users)(_.id)
  }

  protected lazy val tokens = TableQuery[Tokens]
}