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

package uk.gov.hmrc.cip.controllers

import org.slf4j.LoggerFactory
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import uk.gov.hmrc.cip.config.AppConfig
import uk.gov.hmrc.cip.constants.ApplicationConstants.VALID
import uk.gov.hmrc.cip.model.PhoneNumberData.phoneNumberReads
import uk.gov.hmrc.cip.service.PhoneNumberValidationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class ValidatePhoneNumberController @Inject()(cc: ControllerComponents,
                                              service: PhoneNumberValidationService)
  extends AbstractController(cc) {

  private val logger = LoggerFactory.getLogger(getClass)

  def validatePhoneNumber: Action[JsValue] = Action.async(parse.json) {
    request =>
      val phoneNumberResult: JsResult[String] = request.body.validate[String](phoneNumberReads)
      phoneNumberResult match {
        case s: JsSuccess[String] => {
          val result = service.validatePhoneNumber(s.get)
          if (result == VALID) {
            Future.successful(Ok)
          } else {
            logger.warn("Invalid phone number")
            Future.successful(BadRequest(cc.messagesApi("error.invalid")(cc.langs.availables.head)))
          }
        }
        case e: JsError => {
          logger.warn("Errors: " + JsError.toJson(e).toString())}
          Future.successful(BadRequest(cc.messagesApi("error.invalid")(cc.langs.availables.head)))
      }
  }

}
