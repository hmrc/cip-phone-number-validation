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

import scala.util.Try

class PhoneNumberLibraryService() {

  def isValidPhoneNumber(phoneNumber: Option[String]) =
    Try {
      val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
      val phoneNumberProtoBuffer = isUkPhoneNumber(phoneNumber) match {
        case true => phoneNumberUtil.parse(createUKNumber(phoneNumber.get), "UK")
        case false =>
          val numberProto = phoneNumberUtil.parse(phoneNumber.get, "")
          phoneNumberUtil.parse(phoneNumber.get, Integer.toString(numberProto.getCountryCode))
      }
      phoneNumberUtil.isValidNumber(phoneNumberProtoBuffer)
    }

  private def createUKNumber(input: String) = {
    val firstZeroRemoved = input.substring(1)
    "+44" + firstZeroRemoved
  }

  private def isUkPhoneNumber(input: Option[String]) = input match {
    case None => false
    case Some(p) if p.charAt(0) != '0' => false
    case Some(p) if p.charAt(0) == '0' => true
  }
}
