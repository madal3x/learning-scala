import scala.reflect._
import scala.runtime._

abstract class Abs {
  val name: String
}

case class Bar(name: String) extends Abs
case class Baz(name: String) extends Abs


trait Creatable[A]{
  def create(name : String)(implicit m: Manifest[A]) = {
    m.runtimeClass match {
      case a if a == classOf[Bar] => Bar(name)
      case a if a == classOf[Baz] => Baz(name)
    }
  }
}

/*
import reflect.runtime.universe._
def matchContainer[A: TypeTag](c: Container[A]) = c match {
      case c: Container[String] if typeOf[A] <:< typeOf[String] => println("string: " + c.value.toUpperCase)
      case c: Container[Double] if typeOf[A] <:< typeOf[Double] => println("double" + math.sqrt(c.value))
      case c: Container[_] => println("other")
    }
 */

object Bar extends Creatable[Bar]
object Baz extends Creatable[Baz]

// object Bar extends Doable[Bar]

Console println Bar.create("hi")