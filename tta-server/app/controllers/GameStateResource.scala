package controllers

import javax.inject.Inject
import javax.inject.Singleton

import logic.Logic
import models._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class GameStateResource @Inject() () extends Controller {

  var gameState = GameState(
    PlayerIndex(0),
    Map(
      PlayerIndex(0) -> PlayerState(
        buildings = List(Bronze(0)),
        researchedBuildings = List(Bronze(0), Iron(0), Agriculture(0)),
        ore = 0,
        food = 0),
      PlayerIndex(1) -> PlayerState(
        buildings = List(Bronze(0)),
        researchedBuildings = List(Bronze(0), Iron(0), Agriculture(0)),
        ore = 0,
        food = 0)))

  def get = Action {
    Ok(makeResponse(gameState))
  }

  def runAction(actionIdString: String) = Action {
    val actionId = ActionId(actionIdString)
    val derivedGameState = Logic.deriveGameState(gameState)
    val action = derivedGameState.actions(actionId)
    gameState = action.doIt(gameState)
    Ok(makeResponse(gameState))
  }

  def endTurn = Action {
    val activePlayerState = gameState.activePlayerState
    val derivedGameState = Logic.deriveGameState(gameState)
    val activePlayerDerivedState = derivedGameState.derivedPlayerStates(gameState.activePlayerIndex)

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