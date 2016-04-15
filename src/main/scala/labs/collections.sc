// functional combinators map, flatMap, filter ...
val l = List(1, 2, 3, 4)
l.map(_ + 5)

// foreach is for side-effects only, returns Unit
l.foreach(_ + 5)
l.filter(_ % 2 == 0)
val l2 = List(5, 6, 7, 8)
val lz = l.zip(l2)
val lu = l.union(l2)

// splits in two lists based on predicate
lu.partition(_ % 2 == 0)

//finds first element
lu.find(_ % 2 == 0)

//drops first n elements
lu.drop(2)

//drops first elements that abide to predicate, stops at the first non obeying
lu.dropWhile(_ % 2 == 0)
List(List(1, 2, 3), List(4, 5, 6)).flatten

//flatMap is map then flatten
List(List(1, 2, 3), List(4, 5, 6)).flatMap(l => l.map(_ * 2))
// a map is a list of pairs
val ext = Map(("alex", 201), ("bob", 300))
val ext2 = Map("alex" -> 201, "bob" -> 300)

ext == ext2

ext.filter(_._2 % 2 == 0)

ext.filter({case (name, extension) => extension % 2 == 0})
List(1, 2, 3, 4, 5, 6).grouped(2).toList.map {
  case List(x, y) => x * y
}