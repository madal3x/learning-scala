import scalaz._, Scalaz._

// functions are applicative functors
// a function can be considered as a value with a context
// the value is not present yet, and we have to apply that
// function in order to get its value

// the function monad is called the reader monad
// the reader monad allows us to pretend the value is already there
val addStuff: Int => Int = for {
  a <- (_: Int) * 2
  b <- (_: Int) + 10
} yield a + b

// 3*2 + (3+10)
addStuff(3)

val addStuff2 = ({(_: Int) * 2} |@| {(_: Int) + 10}) {_ + _}
addStuff(3)

// without monads
val f1 = (_: Int) * 2
val f2 = (_: Int) + 10
val f3 = (_: Int) + (_: Int)
val addStuff3 = (x: Int) => f3(f1(x), f2(x))
addStuff3(3)

