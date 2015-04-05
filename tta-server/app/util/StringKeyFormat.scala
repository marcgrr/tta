package util

import scala.util.Try

trait StringKeyFormat[T] {
  def reads(stringKey: StringKey): Option[T]
  def writes(obj: T): StringKey
}

object StringKeyFormat {
  def delegateFormat[A, B](
      constructor: A => B,
      destructor: B => Option[A])
      (implicit stringKeyFormat: StringKeyFormat[B]): StringKeyFormat[A] = {
    new StringKeyFormat[A] {
      def reads(stringKey: StringKey): Option[A] = {
        stringKeyFormat.reads(stringKey).flatMap(destructor)
      }

      def writes(obj: A) = {
        stringKeyFormat.writes(constructor(obj))
      }
    }
  }

  implicit val stringFormat: StringKeyFormat[String] = new StringKeyFormat[String] {
    def reads(stringKey: StringKey): Option[String] = Some(stringKey.key)
    def writes(obj: String): StringKey = StringKey(obj)
  }

  implicit val intFormat: StringKeyFormat[Int] = delegateFormat[Int, String](
    int => int.toString,
    string => Try(string.toInt).toOption)
}