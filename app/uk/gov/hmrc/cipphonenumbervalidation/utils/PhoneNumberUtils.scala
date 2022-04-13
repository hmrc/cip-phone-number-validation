package uk.gov.hmrc.cipphonenumbervalidation.utils

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
