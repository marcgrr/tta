package models.cards

import models.Action
import models.ActionId
import models.DeltaPlayerState
import models.DerivedPlayerState
import models.GameState
import util.LeafFormat
import util.LeafyFormat

trait Building extends Tech {
  def derivePlayerState(gameState: GameState): DerivedPlayerState
  def costToBuild: Int

  private def canBuild(gameState: GameState): Boolean = {
    gameState.activePlayerState.ore >= this.costToBuild &&
      gameState.activePlayerState.population >= 1
  }

  override def generateResearchedDerivedPlayerState(gameState: GameState): DerivedPlayerState = {
    val building = this
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
  implicit val leafyFormat: LeafyFormat[Building] = LeafyFormat.middle[Building, Card]
}

trait Mine

case object Bronze extends Building with Mine {
  override val prettyName = "Bronze"
  override val costToBuild: Int = 2
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 1)
  }

  implicit val leafFormat: LeafFormat[Bronze.type] = LeafyFormat.leaf(() => this, prettyName)
}

case object Iron extends Building with Mine {
  override val prettyName = "Iron"
  override val costToBuild: Int = 5
  override val costToResearch: Int = 5

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 2)
  }

  implicit val leafFormat: LeafFormat[Iron.type] = LeafyFormat.leaf(() => this, prettyName)
}

trait Farm

case object Agriculture extends Building with Farm {
  override val prettyName = "Agriculture"
  override val costToBuild: Int = 2
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(foodPerTurn = 1)
  }

  implicit val leafFormat: LeafFormat[Agriculture.type] = LeafyFormat.leaf(() => this, prettyName)
}

trait Lab

case object Philosophy extends Building with Lab {
  override val prettyName = "Philosophy"
  override val costToBuild: Int = 3
  override val costToResearch: Int = 0

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(sciencePerTurn = 1)
  }

  implicit val leafFormat: LeafFormat[Philosophy.type] = LeafyFormat.leaf(() => this, prettyName)
}
