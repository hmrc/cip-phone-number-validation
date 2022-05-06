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
import uk.gov.hmrc.cipphonenumbervalidation.constants.ApplicationConstants.{INVALID, VALID}
import uk.gov.hmrc.cipphonenumbervalidation.utils.PhoneNumberApplicationUtils

import javax.inject.{Inject, Singleton}
import scala.util.{Success}

@Singleton()
class PhoneNumberValidationService @Inject()(phoneNumberLibraryService: PhoneNumberLibraryService) {

  private val logger = LoggerFactory.getLogger(getClass)

  def validatePhoneNumber(phoneNumber: String): String = {

    logger.debug(s"Phone number is being validated ${phoneNumber}")
    val phoneNumberWithNoBannedCharacters = Some(PhoneNumberApplicationUtils.removeNotAllowedCharsFromPhoneNumber(phoneNumber))

    phoneNumberLibraryService.isValidPhoneNumber(phoneNumberWithNoBannedCharacters) match {
      case Success(true) => VALID
      case _ => INVALID
    }
  }
}
