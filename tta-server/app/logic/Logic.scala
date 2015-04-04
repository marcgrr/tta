package logic

import models.Action
import models.ActionId
import models.Bronze
import models.BuildBronze
import models.DerivedGameState
import models.DerivedPlayerState
import models.GameState
import models.PlayerIndex
import models.PlayerState

import scala.collection.immutable

object Logic {
  
  def deriveGameState(gameState: GameState): DerivedGameState = {
    val defaultCandidateActions: immutable.Seq[Action] = List(BuildBronze)

    val allowedActions = defaultCandidateActions.filter { action =>
      action.isValid(gameState)
    }

    val indexedActions = allowedActions.zip(0 to allowedActions.size).map { case (action, index) =>
      ActionId(index.toString) -> action
    }.toMap
    
    DerivedGameState(
      actions = indexedActions,
      derivedPlayerStates = gameState.playerStates.map { case (playerIndex, _) =>
        playerIndex -> derivePlayerState(playerIndex, gameState)
      })
  }
  
  def derivePlayerState(playerIndex: PlayerIndex, gameState: GameState): DerivedPlayerState = {
    val playerState = gameState.playerStates(playerIndex)

    val buildingEffects = playerState.buildings.map(_.derivePlayerState(gameState))

    val allEffects = buildingEffects

    allEffects.fold(DerivedPlayerState.empty)(_ + _)
  }

  def updatePlayerStateAtEndOfTurn(
      playerState: PlayerState,
      derivedPlayerState: DerivedPlayerState): PlayerState = {
    playerState.copy(ore = playerState.ore + derivedPlayerState.orePerTurn)
  }

}