package db.models

import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.UUID

import slick.lifted.MappedTo

import scala.reflect.runtime._
import scala.reflect.runtime.universe._

trait Id[T] {
  def value: T

  override def toString: String = value.toString
}

trait Guid extends Id[String] with MappedTo[String]

trait NumericalId extends MappedTo[Int]

object Guid {

  def next[T <: Guid : TypeTag]: T = {
    val tt = typeTag[T]
    val constructor: MethodSymbol = tt.tpe.members.filter {
      m ⇒
        m.isMethod && m.asMethod.isConstructor
    }.head.asMethod
    currentMirror.reflectClass(tt.tpe.typeSymbol.asClass).reflectConstructor(constructor)(UUID.randomUUID().toString).asInstanceOf[T]
  }

}

case class Credential(value: String) {
  def hash: Array[Byte] = MessageDigest.getInstance("SHA-256").digest(value.getBytes(Charset.forName("UTF-8")))
}
