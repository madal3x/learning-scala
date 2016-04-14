// http://oldfashionedsoftware.com/2009/07/10/scala-code-review-foldleft-and-foldright/

// first b (accumulator) is the first arg of foldLeft ("x", 1, List)
// second b is the result of f with arguments first b and first elem of list
// and so on
// the result is the last b
// foldRight gives stackoverflow for larger lists
List(1,3,5,7).foldLeft("x")((b, a) => b + a)
List(1,3,5,7).foldLeft(1)((b, a) => b + a)
List(1,3,5,7).foldLeft(List[Int]())((b, a) => a :: b)

def test(tuple: Option[Tuple1[Int]]) = {
  tuple.map(_._1).foreach(println)
}

test(None)


(List[Int]() /: List(1,3,5,7))((b, a) => a :: b)