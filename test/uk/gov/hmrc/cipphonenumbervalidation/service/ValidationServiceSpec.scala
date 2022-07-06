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
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.i18n.MessagesApi
import play.api.test.Helpers
import play.api.test.Helpers.{defaultAwaitTimeout, status}

class ValidationServiceSpec extends AnyWordSpec with Matchers {

  "Validate" should {
    "return success if telephone number is valid" in new SetUp {
      val result = validationService.validate("07890349087")
      status(result) shouldBe OK
    }

    "return failure if telephone number is empty" in new SetUp {
      val result = validationService.validate("")
      status(result) shouldBe BAD_REQUEST
    }

    "return failure if telephone number contains letters" in new SetUp {
      val result = validationService.validate("invalid")
      status(result) shouldBe BAD_REQUEST
    }
  }

  trait SetUp {
    implicit val messagesApi: MessagesApi = Helpers.stubMessagesApi()
    implicit val langs = Helpers.stubLangs()
    val validationService = new ValidationService(PhoneNumberUtil.getInstance())(messagesApi, langs)
  }
}
