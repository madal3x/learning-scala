// https://slideslive.com/38908214/differentiable-functional-programming

package labs

object DifProg extends App {
  final case class Dual(value: Double, derivative: Double) {
    def +(that: Dual): Dual =
      Dual(value + that.value, derivative + that.derivative)

    def *(that: Dual): Dual =
      Dual(value * value, value * that.derivative + derivative * that.value)

    def sin: Dual =
      Dual(Math.sin(value), Math.cos(value) * derivative)
  }

  // f(x) = sin^2(x)
  val f = (x: Dual) => x.sin * x.sin
  println(f(Dual(1, 1)))

  import spire.algebra._
  import spire.math._
  import spire.implicits._

  object DualExample {
    def f[A](x: A)(implicit f: Field[A], t: Trig[A]): A =
      sin(x) * sin(x)
  }

  println(DualExample.f(1.0))
  //println(DualExample.f(1.0 + Jet.h(1)))
}
