package models

import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

trait Action {
  def doIt(gameState: GameState): GameState
}

object Action {
  implicit val format: OFormat[Action] = {
    val writes = OWrites { action: Action =>
      Json.obj("This is an" -> "action")
    }
    val reads = Reads { _ =>
      JsSuccess(Building.generateBuildAction(Bronze(0)) : Action)
    }
    OFormat(reads, writes)
  }
}

