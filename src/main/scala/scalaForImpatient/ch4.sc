import scala.collection.immutable.SortedMap

// immutable map
val scores = Map("Alice" -> 19, "Bog" -> 34, "Alex" -> 129)
val bobsScores = scores.getOrElse("Bob", 0)
val alicesScores = if (scores.contains("Alice")) scores("Alice") else 0

for ((k, v) <- scores) println(k, v)

scores.keySet


// sorted map
val sortedScores = SortedMap("Matei" -> 129, "Alice" -> 19, "Bog" -> 34)

// mutable map
val otherScores = scala.collection.mutable.Map((1, 2), (3, 4))
otherScores(1) = 5
otherScores(1)


// tuples
val tuple3 = ("Mooyoo", 42.3232, false)
tuple3._1

val (name, grade, hasPassed) = tuple3
val (first, second, _) = tuple3

// zipping
val symbols = Array("<", ">")
val meaning = Array("lt", "gt")
symbols.zip(meaning)

val productPrices = Map(
  "headphones" -> 124.12,
  "stereo" -> 2124.45
)

productPrices map {
  case (k, v) => (k, v * 2)
}