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

import org.slf4j.LoggerFactory
import uk.gov.hmrc.cip.constants.ApplicationConstants.{INVALID, VALID}
import uk.gov.hmrc.cip.utils.PhoneNumberUtils

import javax.inject.{Inject, Singleton}

@Singleton()
class PhoneNumberValidationService @Inject()(googleLibPhoneNumber: GoogleLibraryWrapper, phoneNumberUtils: PhoneNumberUtils) {

  private val logger = LoggerFactory.getLogger(getClass)

  def validatePhoneNumber(phoneNumber: String): String = {

    logger.debug("Phone number is being validated=" + phoneNumber)
    val phoneNumberWithNoBannedCharacters = phoneNumberUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumber)

    val resultFromGoogleLibrary = googleLibPhoneNumber.isPhoneNumberValidByGoogleLibrary(phoneNumberWithNoBannedCharacters)

    if(resultFromGoogleLibrary) {
      VALID
    } else {
      INVALID
    }

  }

}

