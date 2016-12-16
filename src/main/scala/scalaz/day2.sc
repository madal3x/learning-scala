import rx.lang.scala.Observable

import scalaz._
import Scalaz._
import scala.concurrent.Future

val f: Int => Int = ((x: Int) => x + 1) map {_ * 7}

import scala.language.postfixOps
List(1,2,3).map{3*}

import scala.language.higherKinds
// lift a function of one argument into a context F,
// transforming F[A] to F[B]

// works to apply a function of arity 1 to a value in a context
// for applying a function of arity >1 to a value in a context an Applicative is required
trait Functor[F[_]]  { self =>
  /** Lift `f` into `F` and apply to `F[A]`. */
  def map[A, B](fa: F[A])(f: A => B): F[B]
}




// List[Int] => List[Int]
val l = Functor[List].lift((_: Int) * 3)
l(List(1,2,3))

// List[Int => Int]
// List(1 * _, 2 * _)
val ll = List(1,2).map({(_: Int) * (_: Int)}.curried)
ll map (_(9))

// List(1 * 4, 1 * 5, 1 * 6, 2 * 4, 2 * 5, 2 * 6)
Applicative[List].ap(List(4,5,6))(ll)

// Int => Int => Int
val fun = (x: Int) => (y: Int) => x + y + 10
// Option[Int => Int]
val oo = Option(1).map(fun)
Applicative[Option].ap(Option(2))(oo)
Option(1) <*> (Option(2) <*> Option(fun))


trait Applicative[F[_]] extends Apply[F] { self =>
  def point[A](a: => A): F[A]

  /** alias for `point` */
  def pure[A](a: => A): F[A] = point(a)
}

// extract the function from the F[A=>B] context and
// apply it to the elements from the F[A] context

// apply a value from a context to a lifted function

// It allows applying a wrapped function to a wrapped value

// monadic design is used when you depend on previous computations

// all monads are also applicatives
trait Apply[F[_]] extends Functor[F] { self =>
  def ap[A,B](fa: => F[A])(f: => F[A => B]): F[B]
}

def ListApply: Applicative[List] = new Applicative[List] {
  def map[A, B](a: List[A])(f: A => B): List[B] = a map f
  def unit[A](a: => A): List[A] = List(a)
  def ap[A, B](as: => List[A])(fs: => List[A => B]): List[B] = for {
    a <- as
    f <- fs
  } yield f(a)
  def point[A](a: => A) = unit(a)
}

def OptionApply: Applicative[Option] = new Applicative[Option] {
  def point[A](a: => A) = Option(a)
  def ap[A, B](fa: => Option[A])(f: => Option[(A) => B]) = {
    for {
      a <- fa
      ff <- f
    } yield ff(a)
  }
  def map[A, B](fa: Option[A])(f: (A) => B) =
    fa.map(f)
}

val a1 = { 9.some <*> {(_: Int) + (_: Int)}.curried.some }
3.some <*> a1

// extract values from container and apply to a function
^(3.some, 5.some) {_ + _}
(List("ha", "heh", "hmm") |@| List("?", "!", ".")) {_ + _}

val lll = List("ha", "heh", "hmm") |@| List("?", "!", ".")
