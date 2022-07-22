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

import play.api.Logging
import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.cipphonenumbervalidation.models.{ErrorResponse, PhoneNumber}
import uk.gov.hmrc.cipphonenumbervalidation.service.ValidationService
import uk.gov.hmrc.internalauth.client._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class ValidateController @Inject()(auth: BackendAuthComponents, cc: ControllerComponents, service: ValidationService)
  extends BackendController(cc)
    with Logging {

  val permission = Predicate.Permission(Resource(
    ResourceType("cip-phone-number"),
    ResourceLocation("*")),
    IAAction("*"))

  def validate: Action[JsValue] =
    auth.authorizedAction(permission, Retrieval.username).compose(Action(parse.json)).async { implicit request =>
      val retrieval = request.retrieval.value
      logger.info(s"Calling service is $retrieval")
      withJsonBody[PhoneNumber] {
        phoneNumber => service.validate(phoneNumber.phoneNumber)
      }
    }

  override protected def withJsonBody[T](f: T => Future[Result])(implicit request: Request[JsValue], m: Manifest[T], reads: Reads[T]): Future[Result] = {
    request.body.validate[T] match {
      case JsSuccess(payload, _) => f(payload)
      case JsError(_) =>
        Future.successful(BadRequest(Json.toJson(ErrorResponse("VALIDATION_ERROR", cc.messagesApi("error.invalid")(cc.langs.availables.head)))))
    }
  }
}
