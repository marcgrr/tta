package models

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class PlayerIndex(index: Int){
  def incrementMod(mod: Int): PlayerIndex = {
    PlayerIndex((index + 1) % mod)
  }
}

object PlayerIndex {
  implicit val format: OFormat[PlayerIndex] = Json.format[PlayerIndex]
}