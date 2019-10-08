package labs

import shapeless._

object CoproductSingletons extends App {
  private trait Singletons[T, C <: Coproduct] {
    def values: List[T]
  }

  private object Singletons {
    implicit def cnilSingletons[T]: Singletons[T, CNil] =
      new Singletons[T, CNil] {
        def values = Nil
      }

    implicit def coproductSingletons[T, Head <: T, Tail <: Coproduct](
      implicit
      tsc: Singletons[T, Tail],
      witness: Witness.Aux[Head]): Singletons[T, Head :+: Tail] =

      new Singletons[T, Head :+: Tail] {
        def values = witness.value :: tsc.values
      }
  }

  def generate[T <: scala.Product, C <: Coproduct](
    implicit
    gen: Generic.Aux[T, C],
    singletons: Singletons[T, C]
  ): List[T] = singletons.values
}
