package uk.gov.hmrc.cipphonenumbervalidation.dto

import play.api.libs.json.{Json, OWrites}

case class ErrorResponse(code: String, message: String, details: Option[Map[String, String]] = None)

object ErrorResponse {
  implicit val writes: OWrites[ErrorResponse] = Json.writes[ErrorResponse]
}
