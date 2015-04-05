package models

import play.api.libs.json.Format
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Building {
  def prettyName: String
  def derivePlayerState(gameState: GameState): DerivedPlayerState
  def costToBuild: Int
}

object Building {
  implicit val format: Format[Building] = {
    val writes = Writes { o: Building => JsString(o.prettyName) }
    val reads = Reads { json => JsSuccess(Bronze(0) : Building) }
    Format(reads, writes)
  }

  def generateBuildAction(building: Building): Action = new Action {
    override def doIt(gameState: GameState): GameState = {
      gameState.updatedActivePlayerState ( playerState =>
        playerState.copy(
          buildings = playerState.buildings :+ building,
          population = playerState.population - 1,
          ore = playerState.ore - building.costToBuild)
      )
    }

    override def isValid(gameState: GameState): Boolean = {
      gameState.activePlayerState.ore >= building.costToBuild &&
      gameState.activePlayerState.population >= 1
    }
  }
}

trait Mine

case class Bronze(foo: Int) extends Building with Mine {

  override val prettyName = "Bronze"
  override val costToBuild: Int = 2

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 1)
  }
}

case class Iron(foo: Int) extends Building with Mine {

  override val prettyName = "Iron"
  override val costToBuild: Int = 5

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 2)
  }
}


trait Farm

case class Agriculture(foo: Int) extends Building with Farm {

  override val prettyName = "Agriculture"
  override val costToBuild: Int = 2

  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(foodPerTurn = 1)
  }
}