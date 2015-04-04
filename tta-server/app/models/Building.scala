package models

import play.api.libs.json.Format
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Building {
  def derivePlayerState(gameState: GameState): DerivedPlayerState
}

object Building {
  implicit val format: Format[Building] = {
    val writes = Writes { o: Building => JsString("building") }
    val reads = Reads { json => JsSuccess(Bronze(0) : Building) }
    Format(reads, writes)
  }
}

trait Mine

case class Bronze(foo: Int) extends Building with Mine {
  def derivePlayerState(gameState: GameState): DerivedPlayerState = {
    DerivedPlayerState.empty.copy(orePerTurn = 1)
  }
}

/**


  */