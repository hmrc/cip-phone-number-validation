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

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class PhoneNumberValidationServiceSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens are valid")
  info("So I can check the validity of the details entered")

  val phoneNumberValidationService = new PhoneNumberValidationService()

  Feature("Validate Phone Number") {
    Scenario("Phone number is valid") {

      Given("a phone number is valid")
      val phoneNumber = "01292123456"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be valid")
      assert(actual)
    }

    Scenario("Phone number contains a non-digit character") {

      Given("a phone number contains a non-digit character")
      val phoneNumber = "0a1292123456"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be invalid")
      assert(!actual)
    }

    // TODO - FIX REGEX ISSUEß
   /* Scenario("Phone number contains a dash") {

      Given("a phone number is valid and contains a dash")
      val phoneNumber = "01292-123456"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be valid and have the dash removed")
      assert(actual)
      // TODO - NEED A MOCK AND ARGUMENT CAPTURE
      //assert(actual === 01292123456)
    }*/
  }
}