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

package uk.gov.hmrc.cip.service

import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.when
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import uk.gov.hmrc.cipphonenumbervalidation.utils.PhoneNumberUtils

class PhoneNumberValidationServiceSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens are valid")
  info("So I can check the validity of the details entered")

  val mockGoogleLibraryWrapper = mock[GoogleLibraryWrapper]
  val mockPhoneNumberUtils = mock[PhoneNumberUtils]
  val phoneNumberValidationService = new PhoneNumberValidationService(mockGoogleLibraryWrapper,  mockPhoneNumberUtils)

  val phoneNumber = "01292123456"
  when(mockPhoneNumberUtils.removeNotAllowedCharsFromPhoneNumber(anyString())).thenReturn(phoneNumber)

  Feature("Validate Phone Number") {
    Scenario("Phone number is valid from Google Library") {

      Given("a phone number is valid from Google Library")
      when(mockGoogleLibraryWrapper.isPhoneNumberValidByGoogleLibrary(anyString())).thenReturn(true)

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be valid")
      assert(actual == "Valid")
    }

    Scenario("Phone number is not valid from Google Library") {

      Given("a phone number is not valid from Google Library")
      when(mockGoogleLibraryWrapper.isPhoneNumberValidByGoogleLibrary(anyString())).thenReturn(false)

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be invalid")
      assert(actual != "Valid")
    }

  }

}