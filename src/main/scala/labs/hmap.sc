import java.time.Duration

import shapeless.HMap

import scala.util.{Failure, Success, Try}
import scalaz.NonEmptyList

class BiMapIS[K, V]
implicit val intToString = new BiMapIS[Int, Try[String]]
implicit val stringToInt = new BiMapIS[String, Try[Int]]

def success[T](v: T) : Try[T] = Success(v)
def failure[T](e: Throwable) : Try[T] = Failure(new Error)

val hm = HMap[BiMapIS](23 -> success("foo"), "bar" -> failure[Int](new Error))

def timed[T](f: => T): (T, Duration) = {
  val start = System.nanoTime()
  (f, Duration.ofNanos(System.nanoTime() - start))
}

val map = Map(23 -> 123)

val timeHMapGet = timed {
  val r = hm.get(23)
}

val timedMapGet = timed {
  val rr = map.get(23)
}

hm.get("bar")

// constructor should be private
// should be called ResponseMap
// let HMap be the return so you have access to Option methods
class WrappedHMap(hMap: HMap[BiMapIS]) {
  def get[K, V](k : K)(implicit ev : BiMapIS[K, V]) : V = hMap.get(k).getOrElse(throw new NoSuchElementException(s"No value found for key: $k"))
}

val wrappedHMap = new WrappedHMap(hm)

val rr = wrappedHMap.get(23)
val re = wrappedHMap.get(24)

// put implicits inside companion object ?? which one here?

// put props inside Actor companion object
/*
object ScalaPongActor {
def props(response: String): Props = {
Props(classOf[ScalaPongActor], response)
}
}
 */

sealed trait ValidationError
case class NineDigitsRequired(number: String) extends ValidationError {
  override def toString = s"Number [$number] does not have 9 digits."
}

import scalaz._, Scalaz._

object Validate {
  // needs actual validation
  def nineDigitsNumber(number: String): ValidationNel[ValidationError, String] = number.successNel[ValidationError]
}

final case class OrderNumber private(number: String)

object OrderNumber {
  def apply(number: String): NonEmptyList[ValidationError] \/ OrderNumber = Validate.nineDigitsNumber(number).map(OrderNumber(_)).disjunction
}

val on = OrderNumber("12345")