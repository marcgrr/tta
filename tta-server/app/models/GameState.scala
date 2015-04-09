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

  def getEndTurnActionForActivePlayer: Action = {

    val gameState = this
    new Action {
      override def deltaPlayerState: DeltaPlayerState = {
        gameState.activePlayerState.buildings.map { building =>
          building.deltaPlayerStateAtEndTurnIfBuilt
        }.foldLeft(DeltaPlayerState.empty)(_ + _)
      }
    }

  }

  def getActionsForActivePlayer: Map[ActionId, Action] = {

    // Generate valid play actions from civil cards in hand
    val civilHandPlayActions = this.activePlayerState.civilHand.flatMap { card =>
      card.getPlayAction(this)
    }.toMap

    // Generate valid actions from techs that have been researched
    val techResearchedActions = this.activePlayerState.techs.flatMap { tech =>
      tech.getResearchedAction(this)
    }.toMap

    // Generate increase population action, if valid
    val increasePopulationAction = Population.getIncreasePopulationAction(this)

    // Pool everything together
    val allActions: Map[ActionId, Action] = civilHandPlayActions ++ techResearchedActions ++ increasePopulationAction

    allActions

  }
}

object GameState {
  import util.JsonFormats.Implicits.mapFormat
  implicit val format: OFormat[GameState] = Json.format[GameState]
}