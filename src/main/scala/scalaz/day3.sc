import scalaz._, Scalaz._

val f = Functor[List].lift((_: Int) + 2)

f(List(1,2,3))


// we would have to call .value everywhere
// case class KiloGram(value: Double)

// tagged types
//type Tagged[U] = { type Tag = U }
//type @@[T, U] = T with Tagged[U]

sealed trait KiloGram

// A @@ KiloGram is an infix notation of scalaz.@@[A, KiloGram]
def KiloGram[A](a: A): A @@ KiloGram = Tag[A, KiloGram](a)

val mass = KiloGram(20.0)

// now we have to call .unwrap
2 * Tag.unwrap(mass)

