package util

import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue

import scala.collection.immutable
import scala.reflect.ClassTag
import scala.reflect.classTag
import scala.reflect.runtime.universe.typeOf
import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.Type

/**
 * Lets you create Json Formats for complicated type hierarchies whose leaves are all case objects.
 *
 * To use this, first give all your leaves unique names and use these names to create implicit
 * LeafFormats on them:
 * {{{
 *   case object Leaf1 extends Root {
 *     implicit val leafFormat: LeafFormat[Leaf1.type] = LeafyFormat.leaf(() => Leaf1, "Leaf1")
 *   }
 * }}}
 *
 * Then make an implicit "root" LeafFormat on the "root" of your hierarchy, and tell it about
 * all your leaves, like this:
 * {{{
 *   trait Root
 *
 *   object Root {
 *     implicit leafyFormat: LeafyFormat[Root] = LeafyFormat
 *       .root[Root]
 *       .leaf[Leaf1]
 *       .leaf[Leaf2]
 *       ...
 *       .leaf[LeafN]
 *   }
 * }}}
 *
 * Finally, make implicit "middle" LeafFormats on any "middle" types in your hierarchy, like this:
 * {{{
 *   trait Middle1 extends Root
 *
 *   object Middle1 {
 *     implicit val leafyFormat: LeafyFormat[Middle1] = LeafyFormat.middle[Middle1, Root]
 *   }
 * }}}
 *
 * You only have to made "middle" LeafFormats for "middle" types that you want to read from JSON.
 */
final case class LeafyFormat[R: ClassTag](leaves: immutable.Seq[LeafFormat[_ <: R]])
  extends Format[R] {

  private[this] val nameToConstruct: Map[String, () => _ <: R] = leaves.map { leaf =>
    leaf.name -> leaf.construct
  }.toMap

  private[this] val classToName: Map[Class[_], String] = leaves.map { leaf =>
    leaf.tClass -> leaf.name
  }.toMap

  def leaf[T <: R](implicit leaf: LeafFormat[T]): LeafyFormat[R] = {
    LeafyFormat[R](leaves :+ leaf)
  }

  def reads(json: JsValue): JsResult[R] = {
    json.validate[JsString].flatMap { name =>
      nameToConstruct.get(name.value).map { construct =>
        JsSuccess(construct())
      }.getOrElse {
        JsError(s"$name not found in ${nameToConstruct.keys}.")
      }
    }
  }

  def writes(obj: R): JsValue = {
    val leaf = classToName.getOrElse(
      obj.getClass,
      throw new IllegalArgumentException("You didn't tell me about this class."))
    JsString(leaf)
  }
}

object LeafyFormat {
  def root[R: ClassTag]: LeafyFormat[R] = new LeafyFormat(List.empty)

  def middle[M, P >: M](
    implicit parentLeafyFormat: LeafyFormat[P],
    classTag: ClassTag[M],
    typeTag: TypeTag[M]): LeafyFormat[M] = {
    val leafNames = parentLeafyFormat.leaves.filter { leaf =>
      leaf.tType <:< typeOf[M]
    }
    LeafyFormat[M](leafNames.asInstanceOf[immutable.Seq[LeafFormat[_ <: M]]])
  }

  def leaf[L: ClassTag: TypeTag](construct: () => L, name: String): LeafFormat[L] = {
    LeafFormat(construct, name)
  }
}

final case class LeafFormat[T: ClassTag: TypeTag](construct: () => T, name: String)
  extends Format[T] {

  val tClass: Class[_] = classTag[T].runtimeClass
  val tType: Type = typeOf[T]

  def reads(json: JsValue): JsResult[T] = {
    json match {
      case JsString(nameInJson) if nameInJson == name => JsSuccess(construct())
      case _ => JsError(s"Expected $name, got $json.")
    }
  }

  def writes(obj: T): JsValue = {
    JsString(name)
  }
}
