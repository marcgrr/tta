package logic

import models._

import scala.collection.immutable

object Logic {
  
  def deriveGameState(gameState: GameState): DerivedGameState = {
    DerivedGameState(
      derivedPlayerStates = gameState.playerStates.map { case (playerIndex, _) =>
        playerIndex -> derivePlayerState(playerIndex, gameState)
      })
  }
  
  def derivePlayerState(playerIndex: PlayerIndex, gameState: GameState): DerivedPlayerState = {
    val playerState = gameState.playerStates(playerIndex)

    // Generate valid building actions from researchedBuildings
    val buildActionDerivedPlayerStates = {
      for (researchedBuilding <- gameState.activePlayerState.researchedBuildings)
        yield Building.generateBuildActionDerivedPlayerState(researchedBuilding, gameState)
    }.toList

    // Generate increase population action, if valid
    val increasePopulationDerivedPlayerStates = List(Population.generateIncreasePopulationActionDerivedPlayerState(gameState))

    // Calculate resource gains from buildings you've already built
    val buildingEffects = playerState.buildings.map(_.derivePlayerState(gameState))

    val allEffects = buildActionDerivedPlayerStates ++ increasePopulationDerivedPlayerStates ++ buildingEffects

    allEffects.fold(DerivedPlayerState.empty)(_ + _)
  }

  def updatePlayerStateAtEndOfTurn(
      playerState: PlayerState,
      derivedPlayerState: DerivedPlayerState): PlayerState = {
        playerState.copy(
          ore = playerState.ore + derivedPlayerState.orePerTurn,
          food = playerState.food + derivedPlayerState.foodPerTurn)
  }

}