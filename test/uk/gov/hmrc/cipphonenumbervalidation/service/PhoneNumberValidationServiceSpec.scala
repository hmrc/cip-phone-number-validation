package uk.gov.hmrc.cipphonenumbervalidation.service

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class PhoneNumberValidationServiceSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a HMRC service")
  info("I want to ensure phone numbers entered by citizens are valid")
  info("So I can check the validity of the details entered")

  val phoneNumberValidationService = new PhoneNumberValidationService()

  Feature("Validate Phone Number") {
    Scenario("Phone number is less than 7 digits") {

      Given("a phone number is less than 7 digits")
      val phoneNumber = "012"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be invalid")
      assert(!actual)
    }

    Scenario("Phone number is greater than 20 digits") {

      Given("a phone number is greater than 20 digits")
      val phoneNumber = "012921234567890123456789"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be invalid")
      assert(!actual)
    }

    Scenario("Phone number contains a dash") {

      Given("a phone number is valid and contains a dash")
      val phoneNumber = "01292-123456"

      When("the phone number is validated")
      val actual = phoneNumberValidationService.validatePhoneNumber(phoneNumber)

      Then("the phone number should be valid and have the dash removed")
      assert(actual)
      // TODO - NEED A MOCK AND ARGUMENT CAPTURE
      //assert(actual === 01292123456)
    }
  }
}