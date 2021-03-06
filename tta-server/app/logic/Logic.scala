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

    // Generate valid DerivedPlayerStates from civil cards in hand
    val civilHandDerivedPlayerStates = gameState.activePlayerState.civilHand.map { card =>
      card.generatePlayActionDerivedPlayerState(gameState)
    }.toList

    // Generate valid DerivedPlayerStates from techs that have been researched
    val techsDerivedPlayerStates = gameState.activePlayerState.techs.map { tech =>
      tech.generateResearchedDerivedPlayerState(gameState)
    }.toList

    // Generate increase population action, if valid
    val increasePopulationDerivedPlayerStates =
      List(Population.generateIncreasePopulationActionDerivedPlayerState(gameState))

    // Calculate resource gains from buildings you've already built
    val playerState = gameState.playerStates(playerIndex)
    val buildingEffects = playerState.buildings.map(_.derivePlayerState(gameState))

    // Pool everything together
    val allEffects = civilHandDerivedPlayerStates ++ techsDerivedPlayerStates ++
      increasePopulationDerivedPlayerStates ++ buildingEffects

    allEffects.fold(DerivedPlayerState.empty)(_ + _)
  }

  def updatePlayerStateAtEndOfTurn(
      playerState: PlayerState,
      derivedPlayerState: DerivedPlayerState): PlayerState = {
    playerState.copy(
      ore = playerState.ore + derivedPlayerState.orePerTurn,
      food = playerState.food + derivedPlayerState.foodPerTurn,
      science = playerState.science + derivedPlayerState.sciencePerTurn)
  }

}