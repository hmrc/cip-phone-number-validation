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

package uk.gov.hmrc.cipphonenumbervalidation.utils

import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcCurlRequestLogger

trait DataSteps {
  this: GuiceOneServerPerSuite =>

  protected val wsClient: WSClient = app.injector.instanceOf[WSClient]
  protected val baseUrl = s"http://localhost:$port"
  protected val tokenUrl = s"http://localhost:8470"

  def getToken: String = {
    val response = wsClient
      .url(s"$tokenUrl/test-only/token")
      .withRequestFilter(AhcCurlRequestLogger())
      .post(Json.parse {
        s"""
           {
              "principal": "test-principal",
              "permissions": [{
                "resourceType": "cip-phone-number",
                "resourceLocation": "*",
                "actions": ["*"]
              }]
           }
           """
      }).futureValue
    (response.json \ "token").as[String]
  }
}
