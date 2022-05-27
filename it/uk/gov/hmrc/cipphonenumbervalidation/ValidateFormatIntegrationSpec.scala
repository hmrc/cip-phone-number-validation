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

import org.scalatest.Ignore
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

  "Validate" should {
    "UK National Significant Numbers - respond with 200 status with valid phone number for valid UK numbers" in new SetUp {
        ukNationalSignificantNumbers map { x =>
          val response =
            wsClient
              .url(s"$baseUrl/customer-insight-platform/phone-number/validate-format")
              .post(Json.parse {s"""{"phoneNumber": "$x"}""".stripMargin})
              .futureValue

          response.status shouldBe 200
        }
      }

      "Uk Mobiles - respond with 200 status with valid UK NSN phone number" in new SetUp {
        UkMobiles map { x =>
          val response =
            wsClient
              .url(s"$baseUrl/customer-insight-platform/phone-number/validate-format")
              .post(Json.parse {s"""{"phoneNumber": "$x"}""".stripMargin})
              .futureValue

          response.status shouldBe 200
        }
      }

    "UK Landline Numbers - respond with 200 status with valid UK NSN phone number" in new SetUp {
      ukLandlineNumbers map { x =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate-format")
            .post(Json.parse {s"""{"phoneNumber": "$x"}""".stripMargin})
            .futureValue

        response.status shouldBe 200
      }
    }

    "International numbers - respond with 200 status with valid UK NSN phone number" in new SetUp {
      internationalNumbers map { x =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate-format")
            .post(Json.parse {s"""{"phoneNumber": "$x"}""".stripMargin})
            .futureValue

        response.status shouldBe 200
      }
    }

    "Invalid Numbers - respond with 400 status with invalid phone number" in new SetUp {
      invalidTestData map { x =>
        val response =
          wsClient
            .url(s"$baseUrl/customer-insight-platform/phone-number/validate-format")
            .post(Json.parse {s"""{"phoneNumber": "$x"}""".stripMargin})
            .futureValue

        response.status shouldBe 400
        (response.json \ "message").as[String] shouldBe "Enter a valid telephone number"
      }
    }
  }

  trait SetUp {
    val ukNationalSignificantNumbers = List("0800 11 11", "0845 46 41", "0800 300 3234")
    val UkMobiles = List("07890056734", "0044(0)7890056734", "+44-7890056734")
    val ukLandlineNumbers = List("01372 272357", "020 8221 7300", "+4420 8221 7300", "+44 (0)20 8221 7300")
    val internationalNumbers = List("+1-844-472-4111", "001-844-472-4111", "+1 876-953-2650")
    val invalidTestData = List("999", "asjdh", "112112", "078e5996457", "0785996457e")
  }
}
