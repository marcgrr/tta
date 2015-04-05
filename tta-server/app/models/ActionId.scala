package models

import play.api.libs.json.Format
import util.JsonFormats
import util.StringKeyFormat

case class ActionId(id: String)

object ActionId {
  implicit val stringKeyFormat: StringKeyFormat[ActionId] =
    StringKeyFormat.delegateFormat[ActionId, String](
      actionId => actionId.id,
      string => Some(ActionId(string)))

  implicit val format: Format[ActionId] = JsonFormats.fromStringKeyFormat
}