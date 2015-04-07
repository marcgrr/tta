package models

import play.api.libs.json.Format
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsString
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Tech extends Card {
  def costToResearch: Int
  def generateResearchedDerivedPlayerState(gameState: GameState): DerivedPlayerState
  override def generatePlayActionDerivedPlayerState(gameState:GameState): DerivedPlayerState =
    Tech.generatePlayActionDerivedPlayerState(this, gameState)

}

object Tech {
  implicit val format: Format[Tech] = {
    val writes = Writes { o: Tech => JsString(o.prettyName) }
    val reads = Reads { json => JsSuccess(Bronze(0) : Tech) }
    //TODO: Make it read something sensible
    Format(reads, writes)
  }

  private def canResearch(tech: Tech, gameState: GameState) : Boolean = {
    gameState.activePlayerState.science >= tech.costToResearch &&
      !gameState.activePlayerState.techs.contains(tech)
  }

  def generatePlayActionDerivedPlayerState(tech: Tech, gameState: GameState): DerivedPlayerState = {
    val actions: Map[ActionId, Action] = {
      if (Tech.canResearch(tech, gameState)) {
        val buildAction: Action = new Action {
          override def doIt(gameState: GameState): DeltaPlayerState = DeltaPlayerState.empty.copy(
            newTechs = List(tech),
            science = -tech.costToResearch,
            removedCivilHand = List(tech)
          )
        }
        Map(ActionId("research" + tech.prettyName) -> buildAction)
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