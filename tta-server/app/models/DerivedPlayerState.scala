package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class DerivedPlayerState(
    actions: Map[ActionId, Action],
    orePerTurn: Int,
    foodPerTurn: Int,
    sciencePerTurn: Int) {

  def +(other: DerivedPlayerState): DerivedPlayerState = {
    DerivedPlayerState(
      actions = actions ++ other.actions,
      orePerTurn = orePerTurn + other.orePerTurn,
      foodPerTurn = foodPerTurn + other.foodPerTurn,
      sciencePerTurn = sciencePerTurn + other.sciencePerTurn)
    //TODO we should either validate that actions never clash, or figure out how to combine them
  }

}

object DerivedPlayerState {
  import util.JsonFormats.Implicits.mapFormat
  implicit val format: OFormat[DerivedPlayerState] = Json.format[DerivedPlayerState]

  val empty: DerivedPlayerState = DerivedPlayerState(
    actions = Map.empty,
    orePerTurn = 0,
    foodPerTurn = 0,
    sciencePerTurn = 0)
}