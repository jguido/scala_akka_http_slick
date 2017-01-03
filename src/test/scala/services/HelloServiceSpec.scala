package services

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class HelloServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with HelloService with BeforeAndAfterAll {
  "HelloService" should {
    "Return Hello John" in {
      val attendedResponse = """"Hello John!""""

      Get("/hello?name=John") ~> helloRoute ~> check {
        responseAs[String] shouldEqual attendedResponse
        status shouldEqual StatusCodes.OK
      }
    }
  }
}
