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

import org.mockito.Mockito.when
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatestplus.mockito.MockitoSugar

import scala.util.Try

class PhoneNumberValidationServiceSpec extends AnyFeatureSpec with GivenWhenThen with MockitoSugar {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens are valid")
  info("So I can check the validity of the details entered")

  val mockPhoneNumberLibraryService = mock[PhoneNumberLibraryService]
  val phoneNumberValidationService = new PhoneNumberValidationService(mockPhoneNumberLibraryService)

  val phoneNumber = "01292123456"

  def valid(): Option[String] = Some("01292123456")
  def invalid(): Option[String] = Some("0126")

  Feature("Validate Phone Number") {

    Scenario("Phone number is valid from Library") {

      Given("a phone number is valid from Library")
      when(mockPhoneNumberLibraryService.isValidPhoneNumber(valid())).thenReturn(Try{true})

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber("01292123456")

      Then("the phone number should be invalid")
      assert(actual == "Valid")
    }

    Scenario("Phone number is not valid from Library") {

      Given("a phone number is not valid from Library")
      when(mockPhoneNumberLibraryService.isValidPhoneNumber(invalid())).thenReturn(Try{false})

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber("0126")

      Then("the phone number should be invalid")
      assert(actual != "Valid")
    }

  }

}
