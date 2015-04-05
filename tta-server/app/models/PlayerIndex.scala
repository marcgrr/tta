package models

import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import util.JsonFormats
import util.StringKeyFormat

case class PlayerIndex(index: Int){
  def incrementMod(mod: Int): PlayerIndex = {
    PlayerIndex((index + 1) % mod)
  }
}

object PlayerIndex {
  implicit val stringKeyFormat: StringKeyFormat[PlayerIndex] =
    StringKeyFormat.delegateFormat[PlayerIndex, Int](
      playerIndex => playerIndex.index,
      int => Some(PlayerIndex(int)))

  implicit val format: Format[PlayerIndex] = JsonFormats.fromStringKeyFormat
}