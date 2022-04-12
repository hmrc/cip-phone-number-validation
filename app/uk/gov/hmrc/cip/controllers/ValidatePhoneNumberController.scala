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
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, BaseController, ControllerComponents}
import uk.gov.hmrc.cip.config.AppConfig
import uk.gov.hmrc.cip.model.PhoneNumberData
import uk.gov.hmrc.cip.model.PhoneNumberData.phoneNumberReads
import uk.gov.hmrc.cip.service.PhoneNumberValidationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton()
class ValidatePhoneNumberController @Inject()(cc: ControllerComponents,
                                              langs: Langs,
                                              messagesApi: MessagesApi,
                                              config: AppConfig,
                                              service: PhoneNumberValidationService)
  extends AbstractController(cc) {

  private val logger = LoggerFactory.getLogger(getClass)
//
//def index = Action.async(parse.json)
  //https://stackoverflow.com/questions/35389837/play2-fakerequest-withbodybody-automatically-converted-to-requestanyconten
  def validatePhoneNumber: Action[JsValue] = Action.async(parse.json) {
    request =>
      val phoneNumberResult: JsResult[String] = request.body.validate[String](phoneNumberReads)
      phoneNumberResult match {
        case s: JsSuccess[String] => {
          println("phone number: " + s.get)
          val result = service.validatePhoneNumber(s.get)
          if (result == "Valid") {
            Future.successful(Ok)
          } else {
            Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
          }
        }
        case e: JsError => {
          println("Errors: " + JsError.toJson(e).toString())}
          Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
      }
      /*val maybeJson = Try(Json.parse(request.body))
      maybeJson match {
        case Success(json)      => json.validate[String](phoneNumberReads) match {
          case s: JsSuccess[String] => {
            println("thePhoneNumberToValidate="+ s.get)
            val result = service.validatePhoneNumber(s.get)
            if (result == "Valid") {
              Future.successful(Ok)
            } else {
              Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
            }
          }
          case JsError(errors) =>
            Future.successful(BadRequest(JsError.toJson(errors)))
        }
        case Failure(exception) => Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
      }*/
  }


  /* def validatePhoneNumber: Action[String] = Action.async(parse.tolerantText) {
     request =>
       val maybeJson = Try(Json.parse(request.body))
       maybeJson match {
         case Success(json)      => json.validate[PhoneNumberData] match {
           case JsSuccess(phoneNumberData, _) => {
             val _: PhoneNumberData = phoneNumberData
             val thePhoneNumberToValidate: String = phoneNumberData.phoneNumber
             println("thePhoneNumberToValidate="+ thePhoneNumberToValidate)
             val result = service.validatePhoneNumber(thePhoneNumberToValidate)
             if (result == "Valid") {
               Future.successful(Ok)
             } else {
               Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
             }
           }
           case JsError(errors) =>
             Future.successful(BadRequest(JsError.toJson(errors)))
         }
         case Failure(exception) => Future.successful(BadRequest("""{"obj":[{"msg":["error.payload.missing"],"args":[]}]}"""))
       }
   }
 */
}
