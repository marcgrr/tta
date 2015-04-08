package models

import play.api.libs.json.Format
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsString
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Tech extends Card {
  def costToResearch: Int

  private def canResearch(gameState: GameState): Boolean = {
    gameState.activePlayerState.science >= this.costToResearch &&
      !gameState.activePlayerState.techs.contains(this)
  }

  def generateResearchedDerivedPlayerState(gameState: GameState): DerivedPlayerState

  override def generatePlayActionDerivedPlayerState(gameState:GameState): DerivedPlayerState = {
    def tech = this
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
  implicit val format: Format[Tech] = {
    val writes = Writes { o: Tech => JsString(o.prettyName) }
    val reads = Reads { json => JsSuccess(Bronze(0) : Tech) }
    //TODO: Make it read something sensible
    Format(reads, writes)
  }
}