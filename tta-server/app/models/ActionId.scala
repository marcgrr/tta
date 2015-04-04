package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ActionId(id: String)

object ActionId {
  implicit val format: OFormat[ActionId] = Json.format[ActionId]
}