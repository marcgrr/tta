package models

object Population {

  def costToIncrease(gameState: GameState): Int = {
    // Hard-coded for now. Will depend on gameState once yellow tokens are implemented.
    2
  }

  def canIncrease(gameState: GameState): Boolean = {
    gameState.activePlayerState.food >= costToIncrease(gameState)
  }

  def getIncreasePopulationAction(gameState: GameState): Option[(ActionId, Action)] = {

    if (Population.canIncrease(gameState)) {
      val increasePopulationAction: Action = new Action {
        override def deltaPlayerState: DeltaPlayerState = DeltaPlayerState.empty.copy(
          population = 1,
          food = -costToIncrease(gameState))
      }
      Some(ActionId("increasePopulation") -> increasePopulationAction)
    } else {
      None
    }

  }

}