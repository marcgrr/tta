package models

import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

import scala.collection.immutable

case class DerivedGameState(
    actions: Map[ActionId, Action],
    derivedPlayerStates: Map[PlayerIndex, DerivedPlayerState])

object DerivedGameState {
  private implicit val actions: OFormat[Map[ActionId, Action]] = {
    val writes = OWrites { o: Map[ActionId, Action] =>
      o.toList.foldLeft(Json.obj()) { case (json, (actionId, action)) =>
        json ++ Json.obj(actionId.id -> action)
      }
    }
    val reads = Reads { json =>
      // TODO: Validate correctly.
      json.validate[JsObject].map { jsObject =>
        jsObject.value.map { case (string, jsValue) =>
          ActionId(string) -> jsValue.as[Action]
        }.toMap
      }
    }
    OFormat(reads, writes)
  }

  private implicit val playersFormat: OFormat[Map[PlayerIndex, DerivedPlayerState]] = {
    val writes = OWrites { o: Map[PlayerIndex, DerivedPlayerState] =>
      o.toList.foldLeft(Json.obj()) { case (json, (playerIndex, derivedPlayerState)) =>
        json ++ Json.obj(playerIndex.index.toString -> derivedPlayerState)
      }
    }
    val reads = Reads { json =>
      // TODO: Validate correctly.
      json.validate[JsObject].map { jsObject =>
        jsObject.value.map { case (string, jsValue) =>
          PlayerIndex(string.toInt) -> jsValue.as[DerivedPlayerState]
        }.toMap
      }
    }
    OFormat(reads, writes)
  }

  implicit val format: OFormat[DerivedGameState] = Json.format[DerivedGameState]
}