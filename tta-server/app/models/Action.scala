package models

import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

trait Action {
  def doIt(gameState: GameState): GameState
  def isValid(gameState: GameState): Boolean
}

object Action {
  implicit val format: OFormat[Action] = {
    val writes = OWrites { action: Action =>
      Json.obj("this is a" -> "placeholder")
    }
    val reads = Reads { _ =>
      JsSuccess(BuildBronze : Action)
    }
    OFormat(reads, writes)
  }
}

trait BuildBuilding extends Action {
  val cost: Int
  val building: Building

  override def doIt(gameState: GameState): GameState = {
    gameState.updatedActivePlayerState { playerState =>
      playerState.copy(
        buildings = playerState.buildings :+ building,
        ore = playerState.ore - cost)
    }
  }

  override def isValid(gameState: GameState): Boolean = {
    gameState.activePlayerState.ore >= cost
  }
}

// TODO: Abstract bronze.
case object BuildBronze extends Action with BuildBuilding {
  override val cost: Int = 2
  override val building: Building = Bronze(0)
}