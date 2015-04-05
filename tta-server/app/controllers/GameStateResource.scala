package controllers

import javax.inject.Inject
import javax.inject.Singleton

import logic.Logic
import models.ActionId
import models.GameState
import models.PlayerIndex
import models.PlayerState
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class GameStateResource @Inject() () extends Controller {

  def derivedGameState = Logic.deriveGameState(gameState)
  def activePlayerState = gameState.activePlayerState
  def activePlayerDerivedState = derivedGameState.derivedPlayerStates(gameState.activePlayerIndex)

  var gameState = GameState(
    PlayerIndex(0),
    Map(
      PlayerIndex(0) -> PlayerState.newPlayerState,
      PlayerIndex(1) -> PlayerState.newPlayerState
    )
  )

  def get = Action {
    Ok(makeResponse(gameState))
  }

  def runAction(actionIdString: String) = Action {
    val actionId = ActionId(actionIdString)
    val action = activePlayerDerivedState.actions(actionId)
    gameState = action.doIt(gameState)
    Ok(makeResponse(gameState))
  }

  def endTurn = Action {

    val newActivePlayerState = Logic.updatePlayerStateAtEndOfTurn(activePlayerState, activePlayerDerivedState)

    gameState = gameState.copy(
      activePlayerIndex = gameState.activePlayerIndex.incrementMod(gameState.playerStates.size),
      playerStates = gameState.playerStates.updated(gameState.activePlayerIndex, newActivePlayerState))

    Ok(makeResponse(gameState))
  }

  private[this] def makeResponse(gameState: GameState): JsValue = {
    val derivedGameState = Logic.deriveGameState(gameState)
    Json.obj(
      "gameState" -> gameState,
      "derivedGameState" -> derivedGameState)
  }

}