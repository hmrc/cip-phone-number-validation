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

import org.slf4j.LoggerFactory

import javax.inject.Singleton

@Singleton()
class PhoneNumberValidationService {

  private val logger = LoggerFactory.getLogger(getClass)
  private val notAllowedChars = Array("(", ")", "-", " ")

  def validatePhoneNumber(phoneNumber: String): Boolean = {

    logger.debug("Phone number is being validated=" + phoneNumber)
    if(phoneNumber.isEmpty || phoneNumber.isBlank) {
      logger.debug("Validation Error: Phone number does not exist")
      return false
    }

    val phoneNumberWithNoBannedCharacters = removeNotAllowedCharsFromPhoneNumber(phoneNumber)
    logger.debug("Validation Error: Phone number with not allowed character removed=" + phoneNumberWithNoBannedCharacters)

    if(!isValidLength(phoneNumberWithNoBannedCharacters)) {
      logger.debug("Validation Error: Phone number has invalid length")
      return false
    }

    if(!isAllDigits(phoneNumber)) {
      logger.debug("Validation Error: Phone number has a non-digit character")
      return false
    }
/*

    if(!GoogleLibraryWrapper.isPhonenumberValidByGoogleLibrary(phoneNumberWithNoBannedCharacters)) {
      logger.debug("Validation Error: Phone number is not valid from Google library")
      return false
    }
*/

    logger.debug("Phone number " + phoneNumberWithNoBannedCharacters +" is valid")
    true
  }

  private def isValidLength(input: String): Boolean = {
    var isValid = false
    if(input.length >= 7 || input.length <= 20) {
      isValid = true
    }
    isValid
  }

  private def isAllDigits(input: String): Boolean = {
    input.forall(_.isDigit)
  }

  private def removeNotAllowedCharsFromPhoneNumber(input: String): String = {
    notAllowedChars.foreach { case (element) =>
      input.replaceAll(element, "")
    }
    input
  }

}
