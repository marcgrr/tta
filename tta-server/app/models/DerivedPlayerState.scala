package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import scala.collection.immutable

case class DerivedPlayerState(
    orePerTurn: Int) {

  def +(other: DerivedPlayerState): DerivedPlayerState = {
    DerivedPlayerState(orePerTurn + other.orePerTurn)
  }

}

object DerivedPlayerState {
  implicit val format: OFormat[DerivedPlayerState] = Json.format[DerivedPlayerState]

  val empty: DerivedPlayerState = DerivedPlayerState(
    orePerTurn = 0)
}