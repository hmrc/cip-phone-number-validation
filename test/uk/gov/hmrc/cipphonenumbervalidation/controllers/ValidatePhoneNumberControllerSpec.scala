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

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.{reset, verify, verifyNoInteractions, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.{HeaderNames, Status}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status, stubControllerComponents}
import play.mvc.Http.{HttpVerbs, MimeTypes}
import uk.gov.hmrc.cipphonenumbervalidation.constants.ApplicationConstants.{INVALID, VALID}
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService

class ValidatePhoneNumberControllerSpec extends AnyWordSpec with Matchers with MockitoSugar with BeforeAndAfterEach {

  val expectedErrorMessage: String = "Enter a valid telephone number"
  val mockPhoneNumberValidationService: PhoneNumberValidationService = mock[PhoneNumberValidationService]

  val validatePhoneNumberController = new ValidatePhoneNumberController(stubControllerComponents(), mockPhoneNumberValidationService)

  override def afterEach() {
    reset(mockPhoneNumberValidationService)
  }

  "POST on /customer-insight-platform/phone-number/validate-format" should {
    "return a 200 (Ok) http response when phone number is valid" in {
      val getValidatePhoneNumberRequest = buildRequest("01292123456")
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())).thenReturn(VALID)

      val actual = validatePhoneNumberController.validateFormat()(getValidatePhoneNumberRequest)

      status(actual) shouldBe Status.OK
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe empty
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "return a 400 (Bad Request) http response " +
      "when phone number passes simple validation and does not pass complex validation" in {
      val getValidatePhoneNumberRequest = buildRequest("01292123456")
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())).thenReturn(INVALID)

      val actual = validatePhoneNumberController.validateFormat()(getValidatePhoneNumberRequest)

      status(actual) shouldBe Status.BAD_REQUEST
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "return a 400 (Bad Request) http response when phone number is blank" in {
      val getValidatePhoneNumberRequestInvalidBlank = buildRequest("")

      val actual = validatePhoneNumberController.validateFormat()(getValidatePhoneNumberRequestInvalidBlank)

      status(actual) shouldBe Status.BAD_REQUEST
      verifyNoInteractions(mockPhoneNumberValidationService)
    }

    "return a 400 (Bad Request) http response when phone number is less than minimum length of 7" in {
      val getValidatePhoneNumberRequestInvalidMinLength = buildRequest("012921")

      val actual = validatePhoneNumberController.validateFormat()(getValidatePhoneNumberRequestInvalidMinLength)

      status(actual) shouldBe Status.BAD_REQUEST
      verifyNoInteractions(mockPhoneNumberValidationService)
    }

    "return a 400 (Bad Request) http response when phone number is greater than maximum length of 20" in {
      val getValidatePhoneNumberRequestInvalidMaxLength = buildRequest("012921234567891234567")

      val actual = validatePhoneNumberController.validateFormat()(getValidatePhoneNumberRequestInvalidMaxLength)

      status(actual) shouldBe Status.BAD_REQUEST
      verifyNoInteractions(mockPhoneNumberValidationService)
    }

  }

  private def buildRequest(input: String): FakeRequest[JsValue] = {
    val json: JsObject = buildPayload(input)
    val validatePhoneNumberEndpointUrl: String = "/customer-insight-platform/phone-number/validate-format"
    val request = FakeRequest(HttpVerbs.POST, validatePhoneNumberEndpointUrl)
      .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      .withBody[JsValue](json)
    request
  }

  private def buildPayload(input: String): JsObject = {
    val json: JsObject = Json.obj("phoneNumber" -> input)
    json
  }

}
