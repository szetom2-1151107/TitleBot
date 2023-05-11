package model

import play.api.libs.json.Json

// should favicon be optional????
object TitleResult {
  implicit val format = Json.format[TitleResult]
}
case class TitleResult(title: String, favicon: String)
