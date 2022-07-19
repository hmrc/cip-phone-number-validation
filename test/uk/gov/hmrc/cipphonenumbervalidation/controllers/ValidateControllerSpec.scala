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

import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.prop.TableDrivenPropertyChecks._
import play.api.http.ContentTypes
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{Json, OWrites}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, contentType, defaultAwaitTimeout, status}
import uk.gov.hmrc.cipphonenumbervalidation.models.request.PhoneNumber

class ValidateControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  private val fakeRequest = FakeRequest()
  private lazy val controller = app.injector.instanceOf[ValidateController]
  private implicit val writes: OWrites[PhoneNumber] = Json.writes[PhoneNumber]

  "POST /" should {
    "return 200 with valid telephone number address" in new SetUp {
      forAll(validData) {(enteredPhoneNumber, expectedNormalisedPhoneNumber) =>
        val result = controller.validate()(
          fakeRequest.withBody(Json.toJson(PhoneNumber(enteredPhoneNumber))))
        status(result) shouldBe OK
        contentType(result) mustBe Some(ContentTypes.JSON)
        (contentAsJson(result) \ "phoneNumber").as[String] shouldBe expectedNormalisedPhoneNumber
      }
    }

    "return 400 with empty telephone number address" in new SetUp {
      forAll(invalidData) {(enteredPhoneNumber, expectedErrorMessage) =>
        val result = controller.validate()(
          fakeRequest.withBody(Json.toJson(PhoneNumber(enteredPhoneNumber))))
        status(result) shouldBe BAD_REQUEST
        contentType(result) mustBe Some(ContentTypes.JSON)
        (contentAsJson(result) \ "message").as[String] shouldBe expectedErrorMessage
      }
    }
  }

  trait SetUp {
    val validData = Table(
      ("enteredPhoneNumber", "expectedNormalisedPhoneNumber"),
      ("020 8820 9807", "+442088209807"),
      ("+4420 8820 9807", "+442088209807")
    )

    val invalidData = Table(
      ("enteredPhoneNumber", "expectedErrorMessage"),
      ("+44[0]7890349087", "Enter a valid telephone number"),
      ("020 8e20 9807", "Enter a valid telephone number"),
      ("11011", "Enter a valid telephone number"),
      ("7890349087", "Enter a valid telephone number")
    )

  }
}
