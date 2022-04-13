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

package uk.gov.hmrc.cip.utils

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class PhoneNumberApplicationUtilsSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens have non allowed characters removed")
  info("So I can validate my phone number using external libraries")

  val phoneNumberApplicationUtils = new PhoneNumberApplicationUtils()

  val phoneNumber = "01292123456"

  Feature("Phone number has not allowed characters removed") {

    Scenario("Phone number has the ( character") {

      Given("a phone number contains a (")
      val phoneNumberWithOpenBracket = "(01292123456"

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithOpenBracket)

      Then("the phone number should not contain the (")
      assert(actual == phoneNumber)
    }

    Scenario("Phone number has the ) character") {

      Given("a phone number contains a (")
      val phoneNumberWithClosedBracket = "01292)123456"

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithClosedBracket)

      Then("the phone number should not contain the )")
      assert(actual == phoneNumber)
    }

    Scenario("Phone number has the - character") {

      Given("a phone number contains a -")
      val phoneNumberWithClosedBracket = "01292-123456"

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithClosedBracket)

      Then("the phone number should not contain the -")
      assert(actual == phoneNumber)
    }

    Scenario("Phone number has a blank space in the middle") {

      Given("a phone number contains a blank space in the middle")
      val phoneNumberWithClosedBracket = "01292 123456"

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithClosedBracket)

      Then("the phone number should not contain the blank space")
      assert(actual == phoneNumber)
    }

    Scenario("Phone number has a blank space at the start") {

      Given("a phone number contains a blank space at the start")
      val phoneNumberWithClosedBracket = " 01292123456"

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithClosedBracket)

      Then("the phone number should not contain the blank space")
      assert(actual == phoneNumber)
    }

    Scenario("Phone number has a blank space at the end") {

      Given("a phone number contains a blank space at the end")
      val phoneNumberWithClosedBracket = "01292123456 "

      When("the phone number has not allowed characters removed")
      val actual = phoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumberWithClosedBracket)

      Then("the phone number should not contain the blank space")
      assert(actual == phoneNumber)
    }


  }

}