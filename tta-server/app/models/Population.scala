package models

object Population {

  def costToIncrease(gameState: GameState): Int = {
    // Hard-coded for now. Will depend on gameState once yellow tokens are implemented.
    2
  }

  def canIncrease(gameState: GameState): Boolean = {
    gameState.activePlayerState.food >= costToIncrease(gameState)
  }

  def generateIncreasePopulationAction: Action = new Action {

    override def doIt(gameState: GameState): GameState = {
      gameState.updatedActivePlayerState { playerState =>
        playerState.copy(
          population = playerState.population + 1,
          food = playerState.food - costToIncrease(gameState))
      }
    }

  }


}