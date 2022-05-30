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
import org.apache.commons.lang3.StringUtils
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.mvc.Results.{BadRequest, Ok}
import uk.gov.hmrc.cipphonenumbervalidation.models.ErrorResponse

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton()
class ValidationService @Inject()(cc: ControllerComponents){

  def validate(phoneNumber: String, defaultRegion: String = "GB") = {
    Try {
      val phoneNumberUtil = PhoneNumberUtil.getInstance()

      if (phoneNumber.exists(_.isLetter) || StringUtils.containsAny(phoneNumber,"[]"))
        false
      else
        phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber, defaultRegion))
    } match {
      case Success(true) => Future.successful(Ok)
      case Success(false) => Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", cc.messagesApi("error.invalid")(cc.langs.availables.head)))))
      case Failure(e) => Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", e.getMessage))))
    }
  }
}
