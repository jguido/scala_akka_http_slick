package http.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

trait HelloFormats extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val helloFormat = new RootJsonFormat[Hello] {
    override def read(json: JsValue): Hello = json match {
      case JsString(s) ⇒ Hello(s)
      case _           ⇒ throw new IllegalArgumentException
    }

    override def write(obj: Hello): JsValue = JsString(s"Hello ${obj.name}!")
  }
}

case class Hello(name: String)