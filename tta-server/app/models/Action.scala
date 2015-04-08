package models

import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

trait Action {
  def doIt(gameState: GameState): DeltaPlayerState
}

object Action {
  //TODO: actually make this write and read something sensible
  implicit val format: OFormat[Action] = {
    val writes = OWrites { action: Action =>
      Json.obj("This is an" -> "action")
    }
    val reads = Reads { _ =>
      JsSuccess(empty)
    }
    OFormat(reads, writes)
  }

  val empty: Action = new Action {
    def doIt(gameState: GameState): DeltaPlayerState = DeltaPlayerState.empty
  }
}

