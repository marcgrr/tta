package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class PlayerState(
    buildings: immutable.Seq[Building],
    researchedBuildings: immutable.Seq[Building],
    population: Int,
    ore: Int,
    food: Int)

object PlayerState {
  implicit val format: OFormat[PlayerState] = Json.format[PlayerState]
  val newPlayerState: PlayerState = PlayerState(
      buildings = List(Bronze(0)),
      researchedBuildings = List(Bronze(0), Iron(0), Agriculture(0)),
      ore = 0,
      food = 0,
      population = 1
  )
}