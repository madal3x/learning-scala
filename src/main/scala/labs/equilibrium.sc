import util.control.Breaks._

def solution(A: Array[Int]): Int = {
  val s: Long = A.sum
  var left: Long = 0
  var right: Long = s - A(0)
  if (left == right) 0
  else {
    var eq = -1

    breakable {
      for (p <- 1 until A.length) {
        left += A(p - 1)
        right -= A(p)

        if (left == right) {
          eq = p
          break
        }
      }
    }
    eq
  }
}
solution(Array(-1, 3, -4, 5, 1, -6, 2, 1))