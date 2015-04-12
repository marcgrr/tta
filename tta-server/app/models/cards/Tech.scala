package models.cards

import models.Action
import models.ActionId
import models.DeltaPlayerState
import models.DerivedPlayerState
import models.GameState
import util.LeafyFormat

trait Tech extends Card {
  def costToResearch: Int

  private def canResearch(gameState: GameState): Boolean = {
    gameState.activePlayerState.science >= this.costToResearch &&
      !gameState.activePlayerState.techs.contains(this)
  }

  def generateResearchedDerivedPlayerState(gameState: GameState): DerivedPlayerState

  override def generatePlayActionDerivedPlayerState(gameState:GameState): DerivedPlayerState = {
    val tech = this
    val actions: Map[ActionId, Action] = {
      if (tech.canResearch(gameState)) {
        val researchAction: Action = new Action {
          override def doIt(gameState: GameState): DeltaPlayerState = DeltaPlayerState.empty.copy(
            newTechs = List(tech),
            science = -tech.costToResearch,
            removedCivilHand = List(tech))
        }
        Map(ActionId("research" + tech.prettyName) -> researchAction)
      } else {
        Map.empty
      }
    }

    DerivedPlayerState.empty.copy(actions = actions)
  }

}

object Tech {
  implicit val leafyFormat: LeafyFormat[Tech] = LeafyFormat.middle[Tech, Card]
}