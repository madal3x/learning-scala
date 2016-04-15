import math._

// val - immutable
// var - mutable - need to be instantiated one per line
// def - right side evaluated every time it is accessed
val answerOne = 5
val answerTwo: Int = 5
val greeting, message: String = null
"Hello".intersect("World")
"Hello" intersect "World"
// cast
5.toString
// infix and dot notation
answerOne + answerTwo
answerOne.+(answerTwo)
// mutable vars
var counter = 0
counter += 1
println(counter)
// functions
sqrt(2)
pow(2, 4)
min(3, Pi)
// singleton
BigInt.probablePrime(100, scala.util.Random)

// method without parantheses
"Hello".distinct

// apply
"Hello" (4) // shortcut for ->
"Hello".apply(4)
// construct objects with apply without new
BigInt("1234524")
Array(1, 2, 3, 4, 5)

// exercises
"Hello".count(_.isLower)
"Hello".length
"Harry".patch(1, "ung", 2)
"Hello" * 3
pow(sqrt(3), 2)
// random string (radix is base)
BigInt(200, scala.util.Random).toString(36)
"Hello" (0)
"Hello" ("Hello".length - 1)
// first n elements
"Hello".take(4)
// last n elements
"Hello".takeRight(1)
// drop first n chars
"Hello".drop(2)
