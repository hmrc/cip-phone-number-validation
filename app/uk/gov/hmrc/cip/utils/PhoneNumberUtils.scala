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

package uk.gov.hmrc.cip.utils

import scala.util.matching.Regex

class PhoneNumberUtils {

  val regexOpenBracket = new Regex("[(]")
  val regexCloseBracket = new Regex("[)]")
  val regexDash = new Regex("[^-]")
  val regexBlankSpace = new Regex("[ ]")
  private val notAllowedCharsMap: Map[String, Regex] = Map(
    ("(", regexOpenBracket),
    (")", regexCloseBracket),
    ("-", regexDash),
    ("blank", regexBlankSpace))

  def removeNotAllowedCharsFromPhoneNumber(input: String): String = {
    var badRemoved: String = ""
    notAllowedCharsMap.foreach(x => println("key=" + x._1 + ", value=" + x._2))
    notAllowedCharsMap.foreach { case (element) =>
      badRemoved = element._2.replaceAllIn(input, "")
    }
    badRemoved
  }

}
