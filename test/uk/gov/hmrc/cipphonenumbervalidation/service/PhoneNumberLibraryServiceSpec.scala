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

class PhoneNumberLibraryServiceSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens are valid")
  info("So I can check the validity of the details entered")

  val phoneNumberLibraryService = new PhoneNumberLibraryService()

  val phoneNumber = "01292123456"

  Feature("Validate Phone Number") {
    Scenario("Phone number is valid UK Landline") {
      Given("a phone number is valid UK Landline")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("01292123456"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is valid UK Mobile") {
      Given("a phone number is valid UK Mobile")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("07812345678"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is valid UK Landline WithPlus44") {
      Given("a phone number is valid UK Landline WithPlus44")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("+441292123456"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is valid UK Mobile WithPlus44") {
      Given("a phone number is valid UK Mobile WithPlus44")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("+447812345678"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is valid ROI Landline With Country code") {
      Given("a phone number is valid ROI Landline With Country code")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("+35312382300"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid VOIP number") {
      // real life scam phone number that is in fact a VOIP number
      // its not really in Alloa - carrier network is "Net-Work Internet Ltd"
      Given("a phone number is a VOIP number")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("01259 333036"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid Freephone number") {
      // BT customer services
      Given("a phone number is a Freephone number")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("0800800150"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid Premium rate number") {
      // ITV competition
      Given("a phone number is a Premium rate number")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("09068781119"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid UK NSN number of 11 digits") {
      // Nationwide credit card services
      Given("a phone number is a UK National Significant Number of 11 digits")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("08000 55 66 22"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid UK NSN number of 10 digits") {
      // NHS Inform
      Given("a phone number is a UK National Significant Number of 10 digits")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("0800 22 44 88"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is a valid UK NSN number of 7 digits") {
      // Child line
      Given("a phone number is a UK National Significant Number of 7 digits")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("0800 11 11"))
      Then("the phone number should be valid")
      assert(actual.isSuccess)
    }

    Scenario("Phone number is an invalid number") {
      Given("a phone number is an invalid number")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("35312382300"))
      Then("the phone number should not be valid")
      assert(actual.isFailure)
    }

    Scenario("Phone number is blank") {
      Given("a phone number is an invalid number")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some(""))
      Then("the phone number should not be valid")
      assert(actual.isFailure)
    }

    Scenario("Phone number is empty") {
      Given("a phone number is empty")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some(" "))
      Then("the phone number should not be valid")
      assert(actual.isFailure)
    }

    Scenario("Phone number is not allowed") {
      Given("a phone number is not allowed")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(Some("999"))
      Then("the phone number should not be valid")
      assert(actual.isFailure)
    }

    Scenario("Phone number is none") {
      Given("a phone number is none")
      When("the phone number is validated")
      val actual = phoneNumberLibraryService.isValidPhoneNumber(None)
      Then("the phone number should not be valid")
      assert(actual.isFailure)
    }

  }

}
