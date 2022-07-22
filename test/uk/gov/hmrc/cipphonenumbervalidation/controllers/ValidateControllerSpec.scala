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

import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.Results.Ok
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.cipphonenumbervalidation.models.PhoneNumber
import uk.gov.hmrc.cipphonenumbervalidation.service.ValidationService
import uk.gov.hmrc.internalauth.client.Predicate.Permission
import uk.gov.hmrc.internalauth.client._
import uk.gov.hmrc.internalauth.client.test.{BackendAuthComponentsStub, StubBehaviour}

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.Future

class ValidateControllerSpec extends AnyWordSpec
  with Matchers
  with MockitoSugar {

  "validate" should {
    "return 200 with valid telephone number" in new SetUp {
      validData map { x =>
        when(mockStubBehaviour.stubAuth(Some(expectedPredicate), Retrieval.username)).thenReturn(Future.successful(Retrieval.Username("test-principal")))
        when(validateService.validate(x)).thenReturn(Future.successful(Ok))
        val result = controller.validate(
          fakeRequest.withBody(Json.toJson(PhoneNumber(s"$x"))))
        status(result) shouldBe OK
      }
    }

    "return 400 with empty telephone number" in new SetUp {
      when(mockStubBehaviour.stubAuth(Some(expectedPredicate), Retrieval.username)).thenReturn(Future.successful(Retrieval.Username("test-principal")))
      val result = controller.validate(
        fakeRequest.withBody(Json.toJson(PhoneNumber(""))))
      status(result) shouldBe BAD_REQUEST
      (contentAsJson(result) \ "code").as[String] shouldBe "VALIDATION_ERROR"
      (contentAsJson(result) \ "message").as[String] shouldBe "error.invalid"
    }
  }

  trait SetUp {
    protected val fakeRequest = FakeRequest().withHeaders("Authorization" -> "Token some-token")
    protected val validateService = mock[ValidationService]
    val expectedPredicate = {
      Permission(Resource(ResourceType("cip-phone-number"), ResourceLocation("*")), IAAction("*"))
    }
    protected val mockStubBehaviour = mock[StubBehaviour]
    private val backendAuthComponentsStub = BackendAuthComponentsStub(mockStubBehaviour)(Helpers.stubControllerComponents(), Implicits.global)
    protected val controller = new ValidateController(backendAuthComponentsStub, Helpers.stubControllerComponents(), validateService)
    protected implicit val writes: OWrites[PhoneNumber] = Json.writes[PhoneNumber]

    protected val validData = List("020 8820 9807", "+4420 8820 9807")
  }
}
