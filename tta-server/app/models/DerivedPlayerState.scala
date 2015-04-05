package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class DerivedPlayerState(
    orePerTurn: Int,
    foodPerTurn: Int) {

  def +(other: DerivedPlayerState): DerivedPlayerState = {
    DerivedPlayerState(
      orePerTurn + other.orePerTurn,
      foodPerTurn + other.foodPerTurn
    )
  }

}

object DerivedPlayerState {
  implicit val format: OFormat[DerivedPlayerState] = Json.format[DerivedPlayerState]

  val empty: DerivedPlayerState = DerivedPlayerState(
    orePerTurn = 0,
    foodPerTurn = 0)
}