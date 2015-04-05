package models

object Population {

  def generateIncreasePopulationAction(playerState: PlayerState): Action = new Action{

    val costToBuild = 2

    override def doIt(gameState: GameState): GameState = {
      gameState.updatedActivePlayerState ( playerState =>
        playerState.copy(
          population = playerState.population + 1,
          food = playerState.food - costToBuild)
      )
    }

    override def isValid(gameState: GameState): Boolean = {
      gameState.activePlayerState.food >= costToBuild
    }

  }


}