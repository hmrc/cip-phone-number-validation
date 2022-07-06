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

package uk.gov.hmrc.cipphonenumbervalidation.controllers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{Json, OWrites}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}
import uk.gov.hmrc.cipphonenumbervalidation.models.PhoneNumber

class ValidateControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  private val fakeRequest = FakeRequest()
  private lazy val controller = app.injector.instanceOf[ValidateController]
  private implicit val writes: OWrites[PhoneNumber] = Json.writes[PhoneNumber]

  "POST /" should {
    "return 200 with valid telephone number address" in new SetUp {
      validData map {x =>
        val result = controller.validate()(
          fakeRequest.withBody(Json.toJson(PhoneNumber(s"$x"))))
        status(result) shouldBe OK
      }
    }

    "return 400 with empty telephone number address" in new SetUp {
      invalidData map {x =>
        val result = controller.validate()(
          fakeRequest.withBody(Json.toJson(PhoneNumber(s"$x"))))
        status(result) shouldBe BAD_REQUEST
        (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
      }
    }
  }

  trait SetUp {
    val validData = List("020 8820 9807", "+4420 8820 9807")
    val invalidData = List("+44[0]7890349087", "020 8e20 9807", "11011","")
  }
}
