package controllers

import javax.inject.Inject
import javax.inject.Singleton

import logic.Logic
import models.ActionId
import models.DeltaPlayerState
import models.DerivedGameState
import models.GameState
import models.PlayerIndex
import models.PlayerState

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class GameStateResource @Inject() () extends Controller {

  def activePlayerState = gameState.activePlayerState

  var gameState = GameState(
    PlayerIndex(0),
    Map(
      PlayerIndex(0) -> PlayerState.newPlayerState,
      PlayerIndex(1) -> PlayerState.newPlayerState))

  def get = Action {
    Ok(makeResponse(gameState))
  }

  def runAction(actionIdString: String) = Action {
    val actionId = ActionId(actionIdString)
    val action = gameState.getActionsForActivePlayer(actionId)
    gameState = gameState.updatedActivePlayerState(DeltaPlayerState.applyDeltaPlayerState(action.deltaPlayerState))

    Ok(makeResponse(gameState))
  }

  def endTurn = Action {
    gameState = gameState.updatedActivePlayerState(
      DeltaPlayerState.applyDeltaPlayerState(gameState.getEndTurnActionForActivePlayer.deltaPlayerState)).copy(
      activePlayerIndex = gameState.activePlayerIndex.incrementMod(gameState.playerStates.size))

    Ok(makeResponse(gameState))
  }

  private[this] def makeResponse(gameState: GameState): JsValue = {
    val derivedGameState = DerivedGameState(activePlayerActions = gameState.getActionsForActivePlayer)
    Json.obj(
      "gameState" -> gameState,
      "derivedGameState" -> derivedGameState)
  }

}