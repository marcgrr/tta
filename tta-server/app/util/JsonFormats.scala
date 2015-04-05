package util

import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsObject
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.OWrites
import play.api.libs.json.Reads
import play.api.libs.functional.ApplicativeOps
import play.api.libs.json.Writes

object JsonFormats {

  def fromStringKeyFormat[T](implicit stringKeyFormat: StringKeyFormat[T]): Format[T] = {
    val reads = Reads { json: JsValue =>
      json.validate[JsString].flatMap { case JsString(string) =>
        StringKey(string).asOpt[T].map(JsSuccess(_)).getOrElse {
          JsError(s"Could not read StringKey $string.")
        }
      }
    }

    val writes = Writes { obj: T =>
      JsString(StringKey.toStringKey(obj).key)
    }

    Format(reads, writes)
  }

  object Implicits {
    implicit def mapFormat[K, V](implicit keyFormat: StringKeyFormat[K], valueFormat: Format[V]):
      OFormat[Map[K, V]] = {

      val reads = Reads { json: JsValue =>
        json.validate[JsObject].flatMap { jsObject =>
          jsObject.value.toList.foldLeft(JsSuccess(Map.empty[K, V]): JsResult[Map[K, V]]) {
            case (result, (stringKey, jsValue)) =>
              result.flatMap { map =>
                (StringKey(stringKey).asOpt[K], jsValue.validate[V]) match {
                  case (Some(key), JsSuccess(value, _)) => JsSuccess(map + (key -> value))
                  case (None, _) => JsError(s"Could not read key $stringKey.")
                  case (_, jsError: JsError) => jsError
                }
              }
          }
        }
      }

      val writes = OWrites { map: Map[K, V] =>
        map.toList.foldLeft(Json.obj()) { case (json, (key, value)) =>
          json ++ Json.obj(StringKey.toStringKey(key).key -> Json.toJson(value))
        }
      }

      OFormat(reads, writes)
    }
  }

}