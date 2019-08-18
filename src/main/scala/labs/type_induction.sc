// https://www.youtube.com/watch?v=Nm4OIhjjA2o
// https://tinyurl.com/typeclass-induction

trait Named[E] {
  val name: String
}

implicit val namedInt = new Named[Int] {
  val name = "int"
}

implicit val namedChar = new Named[Char] {
  val name = "char"
}

implicit val namedString = new Named[String] {
  val name = "str"
}

implicitly[Named[Char]].name

type EOL = Unit

implicit val base = new Named[EOL] {
  val name = ""
}

implicit def inductionStep[Head, Tail](
  implicit
  namedHead: Named[Head],
  namedTail: Named[Tail]
) = new Named[(Head, Tail)] {

  val name = s"${namedHead.name}, ${namedTail.name}"
}

implicitly[Named[(Int, (Char, (String, EOL)))]].name



// events dynamic dispatching
trait Parser[A] {
  def parse(msg: String): Option[A]
}

import scala.util.Try
implicit def parser[A] = new Parser[A] {
  def parse(msg: String): Option[A] =
    Try(msg.asInstanceOf[A]).toOption
}

trait DynDisp[E] {
  type Out

  def dispatch(name: String, msg: String): Option[Out]
}

implicit val baseDyn = new DynDisp[EOL] {
  type Out = Nothing

  def dispatch(name: String, msg: String): Option[Nothing] = None
}

implicit def inductionStepDyn[Head, Tail](
  implicit namedHead: Named[Head], parser: Parser[Head], tailDisp: DynDisp[Tail]
) = new DynDisp[(Head, Tail)] {

  type Out = Either[tailDisp.Out, Head]

  def dispatch(name: String, msg: String): Option[Out] =
    if (name == namedHead.name)
      parser.parse(msg).map(Right(_))
    else
      tailDisp.dispatch(name, msg).map(Left(_))
}

val res10 = implicitly[DynDisp[(Int, (Char, (String, EOL)))]]
res10.dispatch("int", "10")
res10.dispatch("char", "b")
res10.dispatch("str", "abc")