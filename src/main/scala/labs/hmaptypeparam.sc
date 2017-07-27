import shapeless.HMap

import scala.util.{Failure, Success, Try}

case class Wrapped[T](t: T)

class BiMapIS[K, V]
implicit val intToString = new BiMapIS[Wrapped[Int], Try[String]]
implicit val stringToInt = new BiMapIS[Wrapped[String], Try[Int]]

def success[T](v: T) : Try[T] = Success(v)
def failure[T](e: Throwable) : Try[T] = Failure(new Error)

val hm = HMap[BiMapIS](Wrapped(23) -> success("foo"), Wrapped("bar") -> failure[Int](new Error))

val r1 = hm.get(Wrapped(23))
val r2 = hm.get(Wrapped("bar"))

def method(wraps: List[Wrapped[_]]) = {

}