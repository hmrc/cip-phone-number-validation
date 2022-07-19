/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.cipphonenumbervalidation

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatest.prop.TableDrivenPropertyChecks._
import play.api.http.ContentTypes
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

class ValidateIntegrationSpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with GuiceOneServerPerSuite {

  private val wsClient = app.injector.instanceOf[WSClient]
  private val baseUrl = s"http://localhost:$port"

  "Validate" should {
    "UK National Significant Numbers - respond with 200 status with valid phone number for valid UK numbers" in new SetUp {
      forAll(ukNationalSignificantNumbers) {(enteredPhoneNumber, expectedPhoneNumber) =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate")
            .post(Json.parse {
              s"""{"phoneNumber": "$enteredPhoneNumber"}""".stripMargin
            })
            .futureValue

        response.status shouldBe 200
        response.contentType shouldBe ContentTypes.JSON
        (response.json \ "phoneNumber").as[String] shouldBe expectedPhoneNumber
      }
    }

    "Uk Mobiles - respond with 200 status with valid UK NSN phone number" in new SetUp {
      forAll(UkMobiles) {(enteredPhoneNumber, expectedPhoneNumber) =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate")
            .post(Json.parse {
              s"""{"phoneNumber": "$enteredPhoneNumber"}""".stripMargin
            })
            .futureValue

        response.status shouldBe 200
        response.contentType shouldBe ContentTypes.JSON
        (response.json \ "phoneNumber").as[String] shouldBe expectedPhoneNumber
      }
    }

    "UK Landline Numbers - respond with 200 status with valid UK NSN phone number" in new SetUp {
      forAll(ukLandlineNumbers) {(enteredPhoneNumber, expectedPhoneNumber) =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate")
            .post(Json.parse {
              s"""{"phoneNumber": "$enteredPhoneNumber"}""".stripMargin
            })
            .futureValue

        response.status shouldBe 200
        response.contentType shouldBe ContentTypes.JSON
        (response.json \ "phoneNumber").as[String] shouldBe expectedPhoneNumber
      }
    }

    "International numbers - respond with 200 status with valid UK NSN phone number" in new SetUp {
      forAll(internationalNumbers) {(enteredPhoneNumber, expectedPhoneNumber) =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate")
            .post(Json.parse {
              s"""{"phoneNumber": "$enteredPhoneNumber"}""".stripMargin
            })
            .futureValue

        response.status shouldBe 200
        response.contentType shouldBe ContentTypes.JSON
        (response.json \ "phoneNumber").as[String] shouldBe expectedPhoneNumber
      }
    }

    "Invalid Numbers - respond with 400 status with invalid phone number" in new SetUp {
      forAll(invalidTestData) {(enteredPhoneNumber, expectedErrorMessage) =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate")
            .post(Json.parse {
              s"""{"phoneNumber": "$enteredPhoneNumber"}""".stripMargin
            })
            .futureValue

        response.status shouldBe 400
        response.contentType shouldBe ContentTypes.JSON
        (response.json \ "message").as[String] shouldBe expectedErrorMessage
      }
    }
  }

  trait SetUp {
    val ukNationalSignificantNumbers = Table(
      ("enteredPhoneNumber", "expectedPhoneNumber"),
      ("0800 11 11", "+448001111"),
      ("0845 46 41", "+448454641"),
      ("0800 300 3234", "+448003003234")
    )

    val UkMobiles = Table(
      ("enteredPhoneNumber", "expectedPhoneNumber"),
      ("07890056734", "+447890056734"),
      ("0044(0)7890056734", "+447890056734"),
      ("+44-7890056734", "+447890056734")
    )

    val ukLandlineNumbers = Table(
      ("enteredPhoneNumber", "expectedPhoneNumber"),
      ("01372 272357", "+441372272357"),
      ("020 8221 7300", "+442082217300"),
      ("+4420 8221 7300", "+442082217300"),
      ("+44 (0)20 8221 7300", "+442082217300")
    )

    val internationalNumbers = Table(
      ("enteredPhoneNumber", "expectedPhoneNumber"),
      ("+1-844-472-4111", "+18444724111"),
      ("001-844-472-4111", "+18444724111"),
      ("+1 876-953-2650", "+18769532650")
    )

    val invalidTestData = Table(
      ("enteredPhoneNumber", "expectedErrorMessage"),
      ("999", "Enter a valid telephone number"),
      ("asjdh", "Enter a valid telephone number"),
      ("112112", "Enter a valid telephone number"),
      ("078e5996457", "Enter a valid telephone number"),
      ("0785996457e", "Enter a valid telephone number"),
      ("7890056734", "Enter a valid telephone number"),
      ("", "Enter a valid telephone number")
    )
  }
}
