package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class PlayerState(
    buildings: immutable.Seq[Building],
    ore: Int)

object PlayerState {
  implicit val format: OFormat[PlayerState] = Json.format[PlayerState]
}