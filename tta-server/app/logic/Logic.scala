package logic

import models._

import scala.collection.immutable

object Logic {
  
  def deriveGameState(gameState: GameState): DerivedGameState = {

    // Generate valid building actions from researchedBuildings
    val buildActionMap = {
      for (researchedBuilding <- gameState.activePlayerState.researchedBuildings; if Building.canBuild(researchedBuilding, gameState))
        yield ActionId("build" + researchedBuilding.prettyName) -> Building.generateBuildAction(researchedBuilding)
    }.toMap

    // Generate increase population action, if valid
    val increasePopulationActionMap = {
      if (Population.canIncrease(gameState))
        Map(ActionId("increasePopulation") -> Population.generateIncreasePopulationAction)
      else
        Map()
    }

    // Pool together all valid actions
    val allowedActions = buildActionMap ++ increasePopulationActionMap

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
          food = playerState.food + derivedPlayerState.foodPerTurn)
  }

}