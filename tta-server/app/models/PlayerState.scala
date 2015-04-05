package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class PlayerState(
    buildings: immutable.Seq[Building],
    researchedBuildings: immutable.Seq[Building],
    ore: Int,
    food: Int)

object PlayerState {
  implicit val format: OFormat[PlayerState] = Json.format[PlayerState]
}