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

package uk.gov.hmrc.cip.controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.{ByteString}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.{verify, when}
import play.api.http.{HeaderNames, Status}
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.Accumulator
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.{FakeRequest, StubControllerComponentsFactory}
import uk.gov.hmrc.cip.config.AppConfig
import uk.gov.hmrc.cip.service.PhoneNumberValidationService
import uk.gov.hmrc.http.HeaderCarrier
import utils.UnitSpec

import scala.concurrent.Future

class ValidatePhoneNumberControllerSpec extends UnitSpec with StubControllerComponentsFactory {

  "POST on /customer-insight-platform/phone-number/validate-details" should {
    "return a 200 (Ok) http response when phone number is valid" in new Setup {
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn "Valid"

      val actual: Accumulator[ByteString, Result] = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequest)

      val accumulatorResult: Future[Result] = actual.run()
      status(accumulatorResult) shouldBe Status.OK
      body(accumulatorResult) shouldBe empty
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response " +
        "when phone number passes simple validation and does not pass complex validation" in new Setup {
        when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn "something else"

        val actual: Accumulator[ByteString, Result] = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequest)

        val accumulatorResult: Future[Result] = actual.run()

        status(accumulatorResult) shouldBe Status.BAD_REQUEST
        //body(accumulatorResult) shouldBe invalidPhoneNumberErrorMsg
        verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is blank" in new Setup {
        val phoneNumberJsonInvalidBlank: JsValue = Json.parse("""
        {
          "phoneNumber" : ""
         }
        """)
        val getValidatePhoneNumberRequestInvalidBlank = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withJsonBody(phoneNumberJsonInvalidBlank)

        val actual: Accumulator[ByteString, Result] = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidBlank)

        val accumulatorResult: Future[Result] = actual.run()

        status(accumulatorResult) shouldBe Status.BAD_REQUEST
        //body(accumulatorResult) shouldBe invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is less than minimum length of 6" in new Setup {
        val phoneNumberJsonInvalidMinLength: JsValue = Json.parse("""
        {
          "phoneNumber" : "01292"
         }
        """)
        val getValidatePhoneNumberRequestInvalidMinLength = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withJsonBody(phoneNumberJsonInvalidMinLength)

        val actual: Accumulator[ByteString, Result] = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMinLength)

        val accumulatorResult: Future[Result] = actual.run()
        status(accumulatorResult) shouldBe Status.BAD_REQUEST
        //body(accumulatorResult) shouldBe invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is greater than maximum length of 20" in new Setup {
        val phoneNumberJsonInvalidMaxLength: JsValue = Json.parse("""
        {
          "phoneNumber" : "012921234567891234567"
         }
        """)
        val getValidatePhoneNumberRequestInvalidMaxLength = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withJsonBody(phoneNumberJsonInvalidMaxLength)

        val actual: Accumulator[ByteString, Result] = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMaxLength)

        val accumulatorResult: Future[Result] = actual.run()
        status(accumulatorResult) shouldBe Status.BAD_REQUEST
        //body(accumulatorResult) shouldBe invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
    }

  }

  private trait Setup {

    implicit val headerCarrier: HeaderCarrier = HeaderCarrier()
    implicit val sys = ActorSystem("MyTest")
    implicit val mat = ActorMaterializer()

    val phoneNumberJson: JsValue = Json.parse(
      """
        |{
        |   "phoneNumber" : "01292123456"
        |}""".stripMargin
    )
  /*  val phoneNumberString: String =
      """
        |{
        |   "phoneNumber" : "01292123456"
        |}""".stripMargin
*/
    val invalidPhoneNumberErrorMsg = "Enter a valid phone number"
    val mockPhoneNumberValidationService: PhoneNumberValidationService = mock[PhoneNumberValidationService]
    val mockControllerComponents: ControllerComponents = stubControllerComponents()

    val mockLangs = mock[Langs]
    val mockMessagesApi = mock[MessagesApi]
    val mockAppConfig = mock[AppConfig]
    val validatePhoneNumberController = new ValidatePhoneNumberController(
      mockControllerComponents
      , mockLangs,mockMessagesApi,mockAppConfig,mockPhoneNumberValidationService)
    val getValidatePhoneNumberRequest = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "application/json").withJsonBody(phoneNumberJson)

   // val getValidatePhoneNumberRequest = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "text/plain").withTextBody(phoneNumberString)
  }

}
