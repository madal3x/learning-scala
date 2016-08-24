/*
Class/trait level val compiles to a combination of a method and a private variable. Hence a recursive definition is allowed.

Local vals on the other hand are just regular variables, and thus recursive definition is not allowed.

http://stackoverflow.com/questions/16257378/is-there-a-generic-way-to-memoize-in-scala
*/

import scala.collection.mutable


case class Memo[A,B](f: A => B) extends (A => B) {
  private val cache = mutable.Map.empty[A, B]
  def apply(x: A) = cache getOrElseUpdate (x, f(x))
}

val fib: Memo[Int, BigInt] = Memo {
  case 0 => 0
  case 1 => 1
  case n => fib(n-1) + fib(n-2)
}

fib(100)