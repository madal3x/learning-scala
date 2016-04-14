def solution(A: Array[Int]): Int = {
  var res = A(0)
  for (i <- 1 until A.length) {
    res = res ^ A(i)
  }

  res
}

def solution2(A: Array[Int]): Int = {
  A.foldLeft(0)((b,a) => b^a)
}
solution(Array(9,3,9,3,7,2,9,9,2))
solution2(Array(9,3,9,3,7,2,9,9,2))