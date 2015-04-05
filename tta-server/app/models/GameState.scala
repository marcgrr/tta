package models

import play.api.libs.json.JsObject
import play.api.libs.json.JsResult
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

import scala.collection.immutable

case class GameState(
    activePlayerIndex: PlayerIndex,
    playerStates: Map[PlayerIndex, PlayerState]) {
  def activePlayerState: PlayerState = {
    playerStates(activePlayerIndex)
  }

  def updatedActivePlayerState(f: PlayerState => PlayerState): GameState = {
    copy(playerStates = playerStates.updated(activePlayerIndex, f(activePlayerState)))
  }
}

object GameState {
  import util.JsonFormats.Implicits.mapFormat
  implicit val format: OFormat[GameState] = Json.format[GameState]
}