package util

case class StringKey(key: String) {
  def asOpt[T](implicit stringKeyFormat: StringKeyFormat[T]): Option[T] = {
    stringKeyFormat.reads(this)
  }
}

object StringKey {
  def toStringKey[T](obj: T)(implicit stringKeyFormat: StringKeyFormat[T]): StringKey = {
    stringKeyFormat.writes(obj)
  }
}