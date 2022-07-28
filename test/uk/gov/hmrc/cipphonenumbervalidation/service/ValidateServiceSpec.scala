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

package uk.gov.hmrc.cipphonenumbervalidation.service

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.ContentTypes
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.test.Helpers.{contentAsJson, contentType, defaultAwaitTimeout, status}

class ValidateServiceSpec extends AnyWordSpec
  with Matchers {

  "validate" should {
    "return success if telephone number is a valid UK mobile number" in new SetUp {
      val result = validateService.validate("07890349087")
      status(result) shouldBe OK
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").as[String] shouldBe "+447890349087"
      (contentAsJson(result) \ "phoneNumberType").as[String] shouldBe "Mobile"
    }

    "return success if telephone number is a valid UK landline number" in new SetUp {
      val result = validateService.validate("01292123456")
      status(result) shouldBe OK
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").as[String] shouldBe "+441292123456"
      (contentAsJson(result) \ "phoneNumberType").as[String] shouldBe "Fixed_line"
    }

    "return success if telephone number is a valid non-UK number" in new SetUp {
      val result = validateService.validate("+35312382300")
      status(result) shouldBe OK
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").as[String] shouldBe "+35312382300"
      (contentAsJson(result) \ "phoneNumberType").as[String] shouldBe "Fixed_line"
    }

    "return failure if telephone number is empty" in new SetUp {
      val result = validateService.validate("")
      status(result) shouldBe BAD_REQUEST
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }

    "return failure if telephone number has no leading zero" in new SetUp {
      val result = validateService.validate("7890349087")
      status(result) shouldBe BAD_REQUEST
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }

    "return failure if telephone number contains letters" in new SetUp {
      val result = validateService.validate("invalid")
      status(result) shouldBe BAD_REQUEST
      contentType(result) shouldBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }

    "return failure if telephone number fails to parse" in new SetUp {
      val result = validateService.validate("xxxxxxxxxx")
      status(result) shouldBe BAD_REQUEST
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "Enter a valid telephone number"
    }
  }

  trait SetUp {
    val validateService = new ValidateService(PhoneNumberUtil.getInstance())
  }
}
