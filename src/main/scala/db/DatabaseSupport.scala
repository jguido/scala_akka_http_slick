package db

import java.sql.{Connection, DriverManager, ResultSet}

import app.config.{AppConfig, DbConfig}
import com.typesafe.config
import config.{Config, ConfigFactory}
import db.models.{Guid, NumericalId}
import db.tools.Migrator
import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer

trait DatabaseSupport {

  import scala.reflect.runtime._
  import scala.reflect.runtime.universe._

  private val load: Config = ConfigFactory.load("application")
  val conf = new AppConfig(load)
  val dbConf = conf.dbConfig



  Class.forName(dbConf.jdbcDriver)

  DatabaseSupport.applyMigrate(dbConf)

  import profile.api._

  lazy val database = Database.forURL(dbConf.url, user = dbConf.username, password = dbConf.password, driver = dbConf.jdbcDriver)

  lazy val profile = dbConf.slickDriver

  def selectSingleCell[T: TypeTag](query: String): T = {
    val producer = producerFor(typeOf[T]) _
    useConnection {
      connection ⇒
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)
        resultSet.next()
        producer(resultSet, 1).asInstanceOf[T]
    }
  }

  def selectSingle[T <: Product: TypeTag](query: String): T = {
    selectMultiple[T](query).head
  }

  def selectMultiple[T <: Product: TypeTag](query: String): List[T] = {
    useConnection {
      connection ⇒
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)
        val results = ListBuffer[T]()
        while (resultSet.next()) {
          val tt = typeTag[T]
          typeOf[T] match {
            case t @ TypeRef(pre, sym, args) ⇒
              val argsWithResultSetIndex = args.zipWithIndex.map { el ⇒ (el._1, el._2 + 1) }
              val theNewArgs = argsWithResultSetIndex.map { el ⇒ producerFor(el._1)(resultSet, el._2) }
              val constructor: MethodSymbol = tt.tpe.members.filter {
                m ⇒
                  m.isMethod && m.asMethod.isConstructor
              }.head.asMethod
              results += currentMirror.reflectClass(tt.tpe.typeSymbol.asClass).reflectConstructor(constructor)(theNewArgs: _*).asInstanceOf[T]
          }
        }
        results.toList
    }
  }

  def producerFor(t: Type)(resultSetRow: ResultSet, position: Int): Any = {
    def isTypeOf(ty: Type, sup: Type): Boolean = {
      ty.baseClasses.foldLeft[Boolean](ty =:= sup) {
        (alreadyMatched, baseType) ⇒
          alreadyMatched || baseType.asType.toType =:= sup
      }
    }
    def handleType(t: Type): Any = {
      t match {
        case ty if ty =:= typeOf[String]             ⇒ resultSetRow.getString(position)
        case ty if ty =:= typeOf[Boolean]            ⇒ resultSetRow.getBoolean(position)
        case ty if ty =:= typeOf[Double]             ⇒ resultSetRow.getDouble(position)
        case ty if ty =:= typeOf[Int]                ⇒ resultSetRow.getInt(position)
        case ty if ty =:= typeOf[Long]               ⇒ resultSetRow.getLong(position)
        case ty if ty =:= typeOf[Array[Byte]]        ⇒ resultSetRow.getBytes(position)
        case ty if ty =:= typeOf[DateTime]           ⇒ new DateTime(resultSetRow.getTimestamp(position))
        case ty if isTypeOf(ty, typeOf[Guid]) ⇒
          val constructor = t.members.filter {
            m ⇒
              m.isMethod && m.asMethod.isConstructor
          }.head.asMethod
          currentMirror.reflectClass(t.typeSymbol.asClass).reflectConstructor(constructor)(resultSetRow.getString(position))
        case ty if isTypeOf(ty, typeOf[NumericalId]) ⇒
          val constructor = t.members.filter {
            m ⇒
              m.isMethod && m.asMethod.isConstructor
          }.head.asMethod
          currentMirror.reflectClass(t.typeSymbol.asClass).reflectConstructor(constructor)(resultSetRow.getInt(position))
        case ty if ty <:< typeOf[Option[Any]] ⇒
          if (resultSetRow.getObject(position) == null) None
          else Some(handleType(ty.typeArgs.head))
      }
    }
    handleType(t)
  }

  def execute(query: String): Boolean = {
    useConnection {
      _.createStatement().execute(query)
    }
  }

  def useConnection[T](fn: Connection ⇒ T): T = {
    val connection = DriverManager.getConnection(dbConf.url, dbConf.username, dbConf.password)
    try {
      fn(connection)
    } catch {
      case e: Exception ⇒
        e.printStackTrace()
        throw e
    } finally {
      connection.close()
    }
  }
}

object DatabaseSupport {

  private var urlVersion = Map[String, Int]()

  def applyMigrate(dbConf: DbConfig): Unit = {
    urlVersion.synchronized {
      if (!urlVersion.isDefinedAt(dbConf.url)) {
        urlVersion = urlVersion + (dbConf.url -> new Migrator(dbConf).migrate())
      }
    }
  }
}

