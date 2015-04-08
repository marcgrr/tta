package models

import play.api.libs.json.Format
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Building extends Tech {
  def derivePlayerState(gameState: GameState): DerivedPlayerState
  def costToBuild: Int

  private def canBuild(gameState: GameState): Boolean = {
    gameState.activePlayerState.ore >= this.costToBuild &&
      gameState.activePlayerState.population >= 1
  }

  override def generateResearchedDerivedPlayerState(gameState: GameState): DerivedPlayerState = {
    def building = this
    val actions: Map[ActionId, Action] = {
      if (building.canBuild(gameState)) {
        val buildAction: Action = new Action {
          override def doIt(gameState: GameState): DeltaPlayerState = DeltaPlayerState.empty.copy(
            newBuildings = List(building),
            population = -1,
            ore = -building.costToBuild)
        }
        Map(ActionId("build" + this.prettyName) -> buildAction)
      } else {
        Map.empty
      }
    }

    DerivedPlayerState.empty.copy(actions = actions)
  }
}

object Building {
  implicit val format: Format[Building] = {
    val writes = Writes { o: Building => JsString(o.prettyName) }
    val reads = Reads { json => JsSuccess(Bronze(0) : Building) }
    //TODO: Make it read something sensible
    Format(reads, writes)
  }
}

trait Mine

case class Bronze(foo: Int) extends Building with Mine {

  override val prettyName = "Bronze"
  override val costToBuild: Int = 2
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 1)
  }
}

case class Iron(foo: Int) extends Building with Mine {

  override val prettyName = "Iron"
  override val costToBuild: Int = 5
  override val costToResearch: Int = 5

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 2)
  }
}


trait Farm

case class Agriculture(foo: Int) extends Building with Farm {

  override val prettyName = "Agriculture"
  override val costToBuild: Int = 2
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(foodPerTurn = 1)
  }
}


trait Lab

case class Philosophy(foo: Int) extends Building with Lab {
  override val prettyName = "Philosophy"
  override val costToBuild: Int = 3
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(sciencePerTurn = 1)
  }
}