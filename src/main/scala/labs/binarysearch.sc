val a = List(1, 3, 6, 8, 10, 14, 18, 24, 28)

def search(a: List[Int], x: Int): Int = {
  def bs(x: Int, min: Int, max: Int): Int = {
    val mid = (max + min) / 2
    if (a(mid) == x)
      mid
    else if (min >= max)
      -1
    else if (x < a(mid))
      bs(x, min, mid - 1)
    else
      bs(x, mid + 1, max)
  }

  bs(x, 0, a.length - 1)
}

search(a, 24)
search(a, 500)