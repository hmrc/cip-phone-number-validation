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

import org.mockito.IdiomaticMockito
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.Results.Ok
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.cipphonenumbervalidation.models.request.PhoneNumber
import uk.gov.hmrc.cipphonenumbervalidation.service.ValidateService

import scala.concurrent.Future
import scala.util.Random

class ValidateControllerSpec extends AnyWordSpec
  with Matchers
  with IdiomaticMockito {

  "validate" should {
    "delegate to validate service" in new SetUp {
      val phoneNumber = "1234567"
      mockValidateService.validate(phoneNumber)
        .returns(Future.successful(Ok))
      val result = controller.validate()(
        fakeRequest.withBody(Json.toJson(PhoneNumber(phoneNumber))))
      status(result) shouldBe OK
      mockValidateService.validate(phoneNumber) was called
    }

    "return 400 with blank telephone number" in new SetUp {
      val phoneNumber = ""
      val result = controller.validate()(
        fakeRequest.withBody(Json.toJson(PhoneNumber(phoneNumber))))
      status(result) shouldBe BAD_REQUEST
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }

    "return 400 with telephone number too long" in new SetUp {
      val phoneNumber = Random.alphanumeric.take(21).mkString
      val result = controller.validate()(
        fakeRequest.withBody(Json.toJson(PhoneNumber(phoneNumber))))
      status(result) shouldBe BAD_REQUEST
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }
  }

  trait SetUp {
    protected val fakeRequest = FakeRequest()
    protected val mockValidateService = mock[ValidateService]
    protected val controller = new ValidateController(Helpers.stubControllerComponents(), mockValidateService)
    protected implicit val writes: OWrites[PhoneNumber] = Json.writes[PhoneNumber]
  }
}
