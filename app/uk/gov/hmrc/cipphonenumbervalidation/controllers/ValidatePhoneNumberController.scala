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

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Result}

import javax.inject.{Inject, Singleton}

@Singleton()
class ValidatePhoneNumberController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def validatePhoneNumber(phoneNumber: String): Action[AnyContent] = Action { implicit request =>
    // TODO - USE LOGBACK AND ADD SERVICE
    println("validating phone number=" + phoneNumber)
    val r: Result = Ok("validating phone number repsonse")
    r
  }

}
