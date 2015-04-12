package models

import models.cards.Agriculture
import models.cards.Bronze
import models.cards.Building
import models.cards.Card
import models.cards.Iron
import models.cards.Philosophy
import models.cards.Tech
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
    buildings = List(Bronze, Bronze, Agriculture, Agriculture, Philosophy),
    techs = List(Bronze, Agriculture, Philosophy),
    civilHand = List(Iron),
    population = 0,
    ore = 2,
    food = 2,
    science = 0)
}