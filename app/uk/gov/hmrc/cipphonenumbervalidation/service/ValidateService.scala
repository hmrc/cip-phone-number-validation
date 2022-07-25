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

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import org.apache.commons.lang3.StringUtils
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, Ok}
import uk.gov.hmrc.cipphonenumbervalidation.models.response.{ErrorResponse, PhoneNumberResponse}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton()
class ValidateService @Inject()(phoneNumberUtil: PhoneNumberUtil) extends Logging {

  private val formatInE164 = (x: PhoneNumber) => phoneNumberUtil.format(x, PhoneNumberFormat.E164)

  def validate(phoneNumber: String)(implicit defaultRegion: String = "GB"): Future[Result] = {
    Try {
      val mandatoryFirstChars = "+0"
      phoneNumber match {
        case _ if phoneNumber.isEmpty || existsLetter(phoneNumber) || containsChars(phoneNumber) ||
          !mandatoryFirstChars.contains(phoneNumber.charAt(0)) => false
        case _ if isValidPhoneNumber(phoneNumber) =>
          PhoneNumberResponse(formatInE164(parsePhoneNumber(phoneNumber)),
            getPhoneNumberType(phoneNumber).name.toLowerCase.capitalize)
      }
    } match {
      case Success(phoneNumberResponse: PhoneNumberResponse) => Future.successful(Ok(Json.toJson(phoneNumberResponse)))
      case Success(false) | Failure(_) =>
        logger.warn("Failed to validate phone number")
        Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", "Enter a valid telephone number"))))
    }
  }

  private def isValidPhoneNumber(phoneNumber: String)(implicit defaultRegion: String) = phoneNumberUtil.isValidNumber(parsePhoneNumber(phoneNumber))

  private def getPhoneNumberType(phoneNumber: String)(implicit defaultRegion: String) = phoneNumberUtil.getNumberType(phoneNumberUtil.parse(phoneNumber, defaultRegion))

  private def existsLetter(phoneNumber: String) = phoneNumber.exists(_.isLetter)

  private def containsChars(phoneNumber: String) = StringUtils.containsAny(phoneNumber, "[]")

  private def parsePhoneNumber(phoneNumber: String)(implicit defaultRegion: String): PhoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion)
}
