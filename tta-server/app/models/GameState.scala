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
  private implicit val playersFormat: OFormat[Map[PlayerIndex, PlayerState]] = {
    val writes = OWrites { o: Map[PlayerIndex, PlayerState] =>
      o.toList.foldLeft(Json.obj()) { case (json, (playerIndex, playerState)) =>
        json ++ Json.obj(playerIndex.index.toString -> playerState)
      }
    }
    val reads = Reads { json =>
      // TODO: Validate correctly.
      json.validate[JsObject].map { jsObject =>
        jsObject.value.map { case (string, jsValue) =>
          PlayerIndex(string.toInt) -> jsValue.as[PlayerState]
        }.toMap
      }
    }
    OFormat(reads, writes)
  }

  implicit val format: OFormat[GameState] = Json.format[GameState]
}