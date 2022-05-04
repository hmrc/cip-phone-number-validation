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

<<<<<<< HEAD
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.{verify, when}
import play.api.http.{HeaderNames, Status}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.ControllerComponents
import play.api.test.{FakeRequest, StubControllerComponentsFactory}
import uk.gov.hmrc.cipphonenumbervalidation.config.AppConfig
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService
import uk.gov.hmrc.http.HeaderCarrier
import testutils.UnitSpec

class ValidatePhoneNumberControllerSpec extends UnitSpec with StubControllerComponentsFactory {

  "POST on /customer-insight-platform/phone-number/validate-details" should {
    "return a 200 (Ok) http response when phone number is valid" in new Setup {
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn "Valid"
=======
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.{reset, verify, verifyNoInteractions, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.{HeaderNames, Status}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import play.mvc.Http.{HttpVerbs, MimeTypes}
import uk.gov.hmrc.cipphonenumbervalidation.constants.ApplicationConstants.{INVALID, VALID}
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService

class ValidatePhoneNumberControllerSpec extends AnyWordSpec with Matchers with MockitoSugar with BeforeAndAfterEach {

  val validatePhoneNumberEndpointUrl: String = "/customer-insight-platform/phone-number/validate-details"
  val expectedErrorMessage: String = "Enter a valid telephone number"
  val mockPhoneNumberValidationService: PhoneNumberValidationService = mock[PhoneNumberValidationService]
  val app = new GuiceApplicationBuilder()
    .overrides(bind[PhoneNumberValidationService].toInstance(mockPhoneNumberValidationService))
    .build()
  val validatePhoneNumberController = app.injector.instanceOf[ValidatePhoneNumberController]

  val phoneNumberJson: JsObject = Json.obj("phoneNumber" -> "01292123456")
  val getValidatePhoneNumberRequest: FakeRequest[JsValue] = FakeRequest(HttpVerbs.POST, validatePhoneNumberEndpointUrl)
    .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
    .withBody[JsValue](phoneNumberJson)

  override def afterEach() {
    reset(mockPhoneNumberValidationService)
  }

  "POST on /customer-insight-platform/phone-number/validate-details" should {
    "return a 200 (Ok) http response when phone number is valid" in {
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn VALID
>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8

      val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequest)

      status(actual) shouldBe Status.OK
<<<<<<< HEAD
      body(actual) shouldBe empty
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response " +
        "when phone number passes simple validation and does not pass complex validation" in new Setup {
        when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn "something else"

        val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequest)

        status(actual) shouldBe Status.BAD_REQUEST
        body(actual) shouldEqual invalidPhoneNumberErrorMsg
        verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is blank" in new Setup {

        val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidBlank)

        status(actual) shouldBe Status.BAD_REQUEST
        body(actual) shouldEqual invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is less than minimum length of 6" in new Setup {

        val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMinLength)

        status(actual) shouldBe Status.BAD_REQUEST
        body(actual) shouldEqual invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
    }

    "POST on /customer-insight-platform/phone-number/validate-details" should {
      "return a 400 (Bad Request) http response when phone number is greater than maximum length of 20" in new Setup {

        val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMaxLength)

        status(actual) shouldBe Status.BAD_REQUEST
        body(actual) shouldEqual invalidPhoneNumberErrorMsg
        verifyNoInteractions(mockPhoneNumberValidationService)
      }
=======
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe empty
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "return a 400 (Bad Request) http response " +
      "when phone number passes simple validation and does not pass complex validation" in {
      when(mockPhoneNumberValidationService.validatePhoneNumber(anyString())) thenReturn INVALID

      val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequest)

      status(actual) shouldBe Status.BAD_REQUEST
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe expectedErrorMessage
      verify(mockPhoneNumberValidationService).validatePhoneNumber(ArgumentMatchers.eq("01292123456"))
    }

    "return a 400 (Bad Request) http response when phone number is blank" in {
      val getValidatePhoneNumberRequestInvalidBlank = buildRequest("")

      val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidBlank)

      status(actual) shouldBe Status.BAD_REQUEST
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe expectedErrorMessage
      verifyNoInteractions(mockPhoneNumberValidationService)
    }

    "return a 400 (Bad Request) http response when phone number is less than minimum length of 6" in {
      val getValidatePhoneNumberRequestInvalidMinLength = buildRequest("01292")

      val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMinLength)

      status(actual) shouldBe Status.BAD_REQUEST
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe expectedErrorMessage
      verifyNoInteractions(mockPhoneNumberValidationService)
    }

    "return a 400 (Bad Request) http response when phone number is greater than maximum length of 20" in {
      val getValidatePhoneNumberRequestInvalidMaxLength = buildRequest("012921234567891234567")

      val actual = validatePhoneNumberController.validatePhoneNumber()(getValidatePhoneNumberRequestInvalidMaxLength)

      status(actual) shouldBe Status.BAD_REQUEST
      val bodyText: String = contentAsString(actual)
      bodyText shouldBe expectedErrorMessage
      verifyNoInteractions(mockPhoneNumberValidationService)
>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
    }

  }

<<<<<<< HEAD
  private trait Setup {

    implicit val headerCarrier: HeaderCarrier = HeaderCarrier()
    implicit val sys = ActorSystem("MyTest")
    implicit val mat = ActorMaterializer()

    val phoneNumberJson: JsObject = Json.obj("phoneNumber"-> "01292123456")
    val getValidatePhoneNumberRequest = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "application/json").withBody[JsValue](phoneNumberJson)

    val phoneNumberJsonInvalidBlank: JsObject = Json.obj("phoneNumber"-> "")
    val getValidatePhoneNumberRequestInvalidBlank = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "application/json").withBody[JsValue](phoneNumberJsonInvalidBlank)

    val phoneNumberJsonInvalidMinLength: JsObject = Json.obj("phoneNumber"-> "01292")
    val getValidatePhoneNumberRequestInvalidMinLength = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "application/json").withBody[JsValue](phoneNumberJsonInvalidMinLength)

    val phoneNumberJsonInvalidMaxLength: JsObject = Json.obj("phoneNumber"-> "012921234567891234567")
    val getValidatePhoneNumberRequestInvalidMaxLength = FakeRequest("POST", "/customer-insight-platform/phone-number/validate-details").withHeaders(HeaderNames.CONTENT_TYPE -> "application/json").withBody[JsValue](phoneNumberJsonInvalidMaxLength)

    val invalidPhoneNumberErrorMsg = "error.invalid"
    val mockPhoneNumberValidationService: PhoneNumberValidationService = mock[PhoneNumberValidationService]
    val mockControllerComponents: ControllerComponents = stubControllerComponents()

    val mockAppConfig = mock[AppConfig]
    val validatePhoneNumberController = new ValidatePhoneNumberController(
      mockControllerComponents, mockPhoneNumberValidationService)
=======
  private def buildRequest(input: String): FakeRequest[JsValue] = {
    val json: JsObject = buildPayload(input)
    val request = FakeRequest(HttpVerbs.POST, validatePhoneNumberEndpointUrl)
      .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      .withBody[JsValue](json)
    request
  }

  private def buildPayload(input: String): JsObject = {
    val json: JsObject = Json.obj("phoneNumber" -> input)
    json
>>>>>>> f1d6bc5dc52cb476e4536da8f744ff0c911c2cc8
  }

}
