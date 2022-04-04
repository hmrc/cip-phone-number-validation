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
import play.api.data.Forms.{single, text}
import play.api.mvc._
import uk.gov.hmrc.cipphonenumbervalidation.service.PhoneNumberValidationService
import uk.gov.hmrc.cipphonenumbervalidation.validation.{PhoneNumberData, PhoneNumberValidation}

import javax.inject.{Inject, Singleton}

@Singleton()
class ValidatePhoneNumberController @Inject()(val controllerComponents: ControllerComponents, phoneNumberValidationService: PhoneNumberValidationService) extends BaseController {

  private val logger = LoggerFactory.getLogger(getClass)

  val invalidPhoneNumber: String = "Enter a valid phone number"

  val form = Form(single("phoneNumber" -> text(minLength=6, maxLength=20)))

  def validatePhoneNumber(phoneNumber: String): Action[AnyContent] = Action { implicit request =>
    logger.debug("validating phone number=" + phoneNumber)
    var r: Result = BadRequest(invalidPhoneNumber)
    val formData = form.bindFromRequest()

    if(formData.hasErrors) {
      r = BadRequest(invalidPhoneNumber)
    }

    val input = formData.data.get("phoneNumber").get
    println("input=" + input)
    if (phoneNumberValidationService.validatePhoneNumber(phoneNumber)) {
      r = Ok("")
    }
    r
  }

}
