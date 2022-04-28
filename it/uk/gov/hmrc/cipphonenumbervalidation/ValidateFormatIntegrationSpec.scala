package uk.gov.hmrc.cipphonenumbervalidation

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

class ValidateFormatIntegrationSpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with GuiceOneServerPerSuite {

  private val wsClient = app.injector.instanceOf[WSClient]
  private val baseUrl = s"http://localhost:$port"

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .configure("metrics.enabled" -> false)
      .configure("auditing.enabled" -> false)
      .build()

  "validate-details" should {
    "respond with 200 status with valid phone number" in {
      val response =
        wsClient
          .url(s"$baseUrl/phone-number/validate-details")
          .post(Json.parse {
            """
              {
                "phoneNumber": "01292123456"
              }
              """.stripMargin
          })
          .futureValue

      response.status shouldBe 200
    }

    "respond with 400 status with invalid phone number" in {
      val response =
        wsClient
          .url(s"$baseUrl/phone-number/validate-details")
          .post(Json.parse {
            """
              {
                "phoneNumber": "999"
              }
              """.stripMargin
          })
          .futureValue

      response.status shouldBe 400
      (response.json \ "details" \ "obj.phoneNumber").as[String] shouldBe "Enter a valid phone number"
    }
  }
}