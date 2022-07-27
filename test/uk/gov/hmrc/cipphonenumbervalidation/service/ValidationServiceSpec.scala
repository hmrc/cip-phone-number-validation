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
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.ContentTypes
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.i18n.{DefaultMessagesApi, MessagesApi}
import play.api.mvc.Result
import play.api.test.Helpers
import play.api.test.Helpers.{contentAsJson, contentType, defaultAwaitTimeout, status}

import scala.concurrent.Future

class ValidationServiceSpec extends AnyWordSpec with Matchers {

  "formatInE164" should {
    "return E.164 formatted UK phone number" in new SetUp {
      val validPhoneNumber: PhoneNumber = PhoneNumberUtil.getInstance().parse("07890349087", "GB")
      val result = validationService.formatInE164(validPhoneNumber)
      result shouldBe "+447890349087"
    }

    "return E.164 formatted non-UK phone number" in new SetUp {
      val validPhoneNumber: PhoneNumber = PhoneNumberUtil.getInstance().parse("+35312382300", "GB")
      val result = validationService.formatInE164(validPhoneNumber)
      result shouldBe "+35312382300"
    }
  }

  "Validate" should {
    "return success if telephone number is a valid UK mobile number" in new SetUp {
      val result: Future[Result] = validationService.validate("07890349087")

      status(result) shouldBe OK
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").asOpt[String] mustBe Some("+447890349087")
    }

    "return success if telephone number is a valid UK landline number" in new SetUp {
      val result = validationService.validate("01292123456")
      status(result) shouldBe OK
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").asOpt[String] mustBe Some("+441292123456")
    }

    "return success if telephone number is a valid non-UK number" in new SetUp {
      val result = validationService.validate("+35312382300")
      status(result) shouldBe OK
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "phoneNumber").asOpt[String] mustBe Some("+35312382300")
    }

    "return failure if telephone number is empty" in new SetUp {
      val result = validationService.validate("")
      status(result) shouldBe BAD_REQUEST
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").asOpt[String] mustBe Some("VALIDATION_ERROR")
      (contentAsJson(result) \ "message").asOpt[String] mustBe Some("Enter a valid telephone number")
    }

    "return failure if telephone number contains no leading zero" in new SetUp {
      val result = validationService.validate("1292123456")
      status(result) shouldBe BAD_REQUEST
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").asOpt[String] mustBe Some("VALIDATION_ERROR")
      (contentAsJson(result) \ "message").asOpt[String] mustBe Some("Enter a valid telephone number")
    }

    "return failure if telephone number contains letters" in new SetUp {
      val result = validationService.validate("invalid")
      status(result) shouldBe BAD_REQUEST
      contentType(result) mustBe Some(ContentTypes.JSON)
      (contentAsJson(result) \ "code").asOpt[String] mustBe Some("VALIDATION_ERROR")
      (contentAsJson(result) \ "message").asOpt[String] mustBe Some("Enter a valid telephone number")
    }
  }

  trait SetUp {
    implicit val messagesApi: MessagesApi = new DefaultMessagesApi(
      Map(
        "en" ->
          Map("error.invalid" -> "Enter a valid telephone number")
      )
    )
    implicit val langs = Helpers.stubLangs()
    val validationService = new ValidationService(PhoneNumberUtil.getInstance())(messagesApi, langs)
  }
}
