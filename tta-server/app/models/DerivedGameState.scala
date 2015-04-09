package models

import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

import scala.collection.immutable

case class DerivedGameState(
    activePlayerActions: Map[ActionId, Action])

object DerivedGameState {
  import util.JsonFormats.Implicits.mapFormat
  implicit val format: OFormat[DerivedGameState] = Json.format[DerivedGameState]
}