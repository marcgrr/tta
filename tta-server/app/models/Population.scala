package models

object Population {

  def costToIncrease(gameState: GameState): Int = {
    // Hard-coded for now. Will depend on gameState once yellow tokens are implemented.
    2
  }

  def canIncrease(gameState: GameState): Boolean = {
    gameState.activePlayerState.food >= costToIncrease(gameState)
  }

  def generateIncreasePopulationActionDerivedPlayerState(gameState: GameState): DerivedPlayerState = {

    val actions: Map[ActionId, Action] = {
      if (Population.canIncrease(gameState)) {
        val increasePopulationAction: Action = new Action {
          override def doIt(gameState: GameState): DeltaPlayerState = DeltaPlayerState.empty.copy(
            population = 1,
            food = -costToIncrease(gameState)
          )
        }
        Map(ActionId("increasePopulation") -> increasePopulationAction)
      }
      else {
        Map.empty
      }
    }

    DerivedPlayerState.empty.copy(
      actions = actions
    )
  }

}