package models

import play.api.libs.json.Format
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsString
import play.api.libs.json.Reads
import play.api.libs.json.Writes

trait Card {
  def prettyName: String
  def getPlayAction(gameState:GameState): Option[(ActionId, Action)]
}

object Card {

  implicit val format: Format[Card] = {
    val writes = Writes { o: Card => JsString(o.prettyName) }
    val reads = Reads { json => JsSuccess(Bronze(0) : Card) }
    //TODO: Make it read something sensible
    Format(reads, writes)
  }

}