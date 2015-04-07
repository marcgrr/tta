package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class PlayerState(
    buildings: immutable.Seq[Building],
    techs: immutable.Seq[Tech],
    civilHand: immutable.Seq[Card],
    population: Int,
    ore: Int,
    food: Int,
    science: Int)

object PlayerState {
  implicit val format: OFormat[PlayerState] = Json.format[PlayerState]
  val newPlayerState: PlayerState = PlayerState(
      buildings = List(Bronze(0), Bronze(0), Agriculture(0), Agriculture(0), Philosophy(0)),
      techs = List(Bronze(0), Agriculture(0), Philosophy(0)),
      civilHand = List(Iron(0)),
      population = 0,
      ore = 2,
      food = 2,
      science = 0)
}