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
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.mvc._
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService
import uk.gov.hmrc.cipphonenumbervalidation.validation.PhoneNumberData

import javax.inject.{Inject, Singleton}

@Singleton()
class ValidatePhoneNumberController @Inject()(val controllerComponents: ControllerComponents, phoneNumberValidationService: PhoneNumberValidationService) extends BaseController {

  private val logger = LoggerFactory.getLogger(getClass)

  val invalidPhoneNumber: String = "Enter a valid phone number"

  val phoneForm = Form(
    mapping(
      "phoneNumber" -> nonEmptyText(minLength = 6, maxLength = 20)
    )(PhoneNumberData.apply)(PhoneNumberData.unapply)
  )

  def validatePhoneNumber(): Action[AnyContent] = Action { implicit request =>
    logger.debug("validating phone number")
    phoneForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(invalidPhoneNumber)
      },
      formData => {
        /* binding success, you get the actual value. */
        val enteredPhoneNumber = formData.phoneNumber
        logger.debug("entered Phone number is " + enteredPhoneNumber)
        if (phoneNumberValidationService.validatePhoneNumber(enteredPhoneNumber)) {
          Ok("")
        } else {
          BadRequest(invalidPhoneNumber)
        }
      }
    )
  }

}
