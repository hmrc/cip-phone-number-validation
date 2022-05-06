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

package uk.gov.hmrc.cipphonenumbervalidation.controllers

import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc.{Action, ControllerComponents, Request, Result}
import uk.gov.hmrc.cipphonenumbervalidation.constants.ApplicationConstants.{INVALID, VALID}
import uk.gov.hmrc.cipphonenumbervalidation.dto.PhoneNumberDto.phoneNumberReads
import uk.gov.hmrc.cipphonenumbervalidation.dto.{ErrorResponse, PhoneNumberDto}
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton()
class ValidatePhoneNumberController @Inject()(cc: ControllerComponents, service: PhoneNumberValidationService)
  extends BackendController(cc) {

  private val logger = LoggerFactory.getLogger(getClass)

  def validateFormat: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[PhoneNumberDto] { futureResultOutcome }
  }
// TODO - BH - ADD MESSAGE API
  private def futureResultOutcome(t: PhoneNumberDto): Future[Result] = {
    service.validatePhoneNumber(t.phoneNumber) match {
      case VALID => Future.successful(Ok)
      case INVALID => Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", "Enter a valid telephone number"))))
    }
  }

  override protected def withJsonBody[T](f: T => Future[Result])(implicit request: Request[JsValue], m: Manifest[T], reads: Reads[T]) = {
    logger.debug("validating phone number")
    Try(request.body.validate[T]) match {
      case Success(JsSuccess(payload, _)) => f(payload)
      case Success(JsError(_)) =>
        Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", "Enter a valid telephone number"))))
      case Failure(e) =>
        Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", e.getMessage))))
    }
  }

}

