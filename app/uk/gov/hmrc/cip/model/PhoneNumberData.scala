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

package uk.gov.hmrc.cip.model

import play.api.libs.json._

case class PhoneNumberData(phoneNumber: String)

object PhoneNumberData {

  implicit object PhoneNumberDataFormat extends Format[PhoneNumberData] {

    // convert from JSON string to a PhoneNumberData object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[PhoneNumberData] = {
      val phoneNumber = (json \ "phoneNumber").as[String]
      JsSuccess(PhoneNumberData(phoneNumber))
    }

    // convert from PhoneNumberData object to JSON (serializing to JSON)
    def writes(p: PhoneNumberData): JsValue = {
      // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
      val data = Seq("phoneNumber" -> JsString(p.phoneNumber))
      JsObject(data)
    }

  }



}
