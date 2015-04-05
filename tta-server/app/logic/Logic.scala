package logic

import models._

import scala.collection.immutable

object Logic {
  
  def deriveGameState(gameState: GameState): DerivedGameState = {
    val defaultCandidateActionMap = gameState.activePlayerState.researchedBuildings.map (
      building => {
        ActionId("build" + building.prettyName) -> Building.generateBuildAction(building)
      } //why does flatMap have an error here?
    ).toMap

    val allowedActions = defaultCandidateActionMap.filter { case (index, action)=>
      action.isValid(gameState)
    }

    DerivedGameState(
      actions = allowedActions,
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
    playerState.copy(
      ore = playerState.ore + derivedPlayerState.orePerTurn,
      food = playerState.food + derivedPlayerState.foodPerTurn
    )
  }

}