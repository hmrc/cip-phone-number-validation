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

import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.when
import play.api.http.Status
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.{FakeRequest, StubControllerComponentsFactory}
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService
import uk.gov.hmrc.http.HeaderCarrier
import utils.UnitSpec

import scala.concurrent.Future

class ValidatePhoneNumberControllerSpec extends UnitSpec with StubControllerComponentsFactory {

  "GET on /customer-insight-platform/phone-number/validate-details/:phoneNumber" should {
    "return a 200 (Ok) http response" in new Setup {
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn true

      val expected: Future[Result] = validatePhoneNumberController.validatePhoneNumber(phoneNumber)(getValidatePhoneNumberRequest)
      status(expected) shouldBe Status.OK
    }

    "GET on /customer-insight-platform/phone-number/validate-details/:phoneNumber" should {
      "return a 400 (Bad Request) http response" in new Setup {
        when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn false
        val expected: Future[Result] = validatePhoneNumberController.validatePhoneNumber(phoneNumber)(getValidatePhoneNumberRequest)
        status(expected) shouldBe Status.BAD_REQUEST
      }
    }
  }

  private trait Setup {

    implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

    val invalidPhoneNumberErrorMsg = "Enter a valid phone number"
    val phoneNumber = "441292123456"
    val mockPhoneNumberValidationService: PhoneNumberValidationService = mock[PhoneNumberValidationService]
    val mockControllerComponents: ControllerComponents = stubControllerComponents()
    val validatePhoneNumberController = new ValidatePhoneNumberController(mockControllerComponents, mockPhoneNumberValidationService)
    val getValidatePhoneNumberRequest = FakeRequest("GET", "/customer-insight-platform/phone-number/validate-details/:phoneNumber")

  }

}
