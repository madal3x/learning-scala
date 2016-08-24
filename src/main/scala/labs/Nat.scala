// http://rudairandamacha.blogspot.de/2012/02/type-level-programming.html

package labs

trait Nat
trait Succ[N <: Nat] extends Nat

object Nat {
  class _0 extends Nat
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]
  type _6 = Succ[_5]

  def toInt[N <: Nat](implicit iv: ToInt[N]): Int = iv()
  def toInt[A <: Nat, B <: Nat](f: Fact[A])
                               (implicit ev: FactAux[A, B], iv: ToInt[B]) = iv()

  def toIntFact[A <: Nat, B <: Nat](n: A)(implicit evf: FactAux[A, B], iv: ToInt[B]): Int = iv()
}

trait ToInt[N <: Nat] {
  def apply(): Int
}

object ToInt {
  import Nat._0

  // type              value
  // n = 0      =>     0
  implicit val toInt0 = new ToInt[_0] {
    def apply(): Int = 0
  }

  // n -> n+1    =>    v(n+1) = v(n) + 1
  implicit def toIntN[N <: Nat](implicit iv: ToInt[N]): ToInt[Succ[N]] =
    new ToInt[Succ[N]] {
      def apply(): Int = iv() + 1
    }
}

trait SumAux[A <: Nat, B <: Nat, C <: Nat]

object SumAux {
  import Nat._0

  // a + 0 = a
  implicit def sum0[A <: Nat] = new SumAux[A, _0, A] {}

  // a' + b = a + b'
  implicit def sumN[A <: Nat, B <: Nat, C <: Nat]
    (implicit ev: SumAux[Succ[A], B, C]) =
    new SumAux[A, Succ[B], C] {}
}

trait ProdAux[A <: Nat, B <: Nat, C <: Nat]

object ProdAux {
  import Nat._0

  // a * 0 = 0
  implicit def prod0[A <: Nat] = new ProdAux[A, _0, _0] {}

  // a * b' = a * b + a   = d // c = a * b
  implicit def prodN[A <: Nat, B <: Nat, C <: Nat, D <: Nat]
    (implicit evp: ProdAux[A, B, C], evs: SumAux[A, C, D]) =
    new ProdAux[A, Succ[B], D] {}
}

trait FactAux[A <: Nat, B <: Nat]

object FactAux {
  import Nat.{_0, _1}

  // 0! = 1
  implicit val fact0 = new FactAux[_0, _1] {}

  // (n + 1)! = n! * (n + 1)
  implicit def factN[A <: Nat, B <: Nat, C <: Nat]
    (implicit evf: FactAux[A, B], evp: ProdAux[B, Succ[A], C]) =
    new FactAux[Succ[A], C] {}
}

trait Fact[A <: Nat] {
  type Out <: Nat
}

object Fact {
  implicit def fact[A <: Nat, B <: Nat](implicit ev: FactAux[A, B]) =
    new Fact[A] { type Out = B }
}

object Main {
  import Nat._

  def main(args: Array[String]) {
    Console println toInt[_6]

    implicitly[SumAux[_2, _3, _5]]
    //implicitly[SumAux[_2, _3, _6]]

    implicitly[ProdAux[_2, _3, _6]]
    //implicitly[ProdAux[_2, _3, _5]]

    val x: _4 = null

    println(toIntFact(x))

    Console println toInt(implicitly[Fact[_4]])
  }
}