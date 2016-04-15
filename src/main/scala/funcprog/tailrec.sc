import scala.annotation.tailrec

val lessThan = new Function2[Int, Int, Boolean] {
  def apply(a: Int, b: Int): Boolean = a < b
}

fib(5)

def fib(n: Int): Int = {
  @annotation.tailrec
  def loop(n: Int, a: Int, b: Int): Int = {
    if (n == 1) b
    else loop(n - 1, b, a + b)
  }
  loop(n, 0, 1)
}

def factorial(n: Int): Int = {
  @annotation.tailrec
  def loop(n: Int, acc: Int): Int = {
    if (n == 0) acc
    else loop(n - 1, acc * n)
  }

  loop(n, 1)
}
println(formatResult("absolute value", -42, Math.abs))
println(formatResult("factorial", 7, factorial))
println(formatResult("increment", 7, (x: Int) => x + 1))
println(formatResult("increment2", 7, (x) => x + 1))
println(formatResult("increment3", 7, x => x + 1))
println(formatResult("increment4", 7, _ + 1))
println(formatResult("increment5", 7, x => {
  val r = x + 1; r
}))
def formatResult(name: String, n: Int, f: Int => Int) = {
  val msg = "The %s of %d is %d."
  msg.format(name, n, f(n))
}
lessThan(1, 2)
lessThan(2, 1)
def binarySearch(a: List[Int], n: Int): Int = {
  def loop(l: Int, h: Int): Int = {
    if (l > h) -1
    val mid = (l + h) / 2
    if (n == a(mid)) mid
    else if (n < a(mid)) loop(l, mid - 1)
    else loop(mid + 1, h)
  }

  loop(0, a.length - 1)
}

binarySearch(List(1, 2, 3, 4, 5, 6, 7), 6)
binarySearch(List(1, 2, 3, 4, 5, 6, 7), 8)

@tailrec
def isSorted[A](a: Array[A], gt: (A, A) => Boolean): Boolean = {
  if (a.length == 1 || a.length == 0) true
  else if (gt(a(0), a(1))) false
  else isSorted(a.tail, gt)
}

isSorted(Array("b", "a", "c"), (a: String, b: String) => if (a > b) true else false)