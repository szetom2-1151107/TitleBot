package model

import play.api.libs.json.Json

/**
 * Json structure for the result that is sent to the client. If we want to add more information to send to the client, we
 * can easily add parameters to the case class
 */
object TitleResult {
  implicit val format = Json.format[TitleResult]
}
case class TitleResult(title: String, favicon: String)
