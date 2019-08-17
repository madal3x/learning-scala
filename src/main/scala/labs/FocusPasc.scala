package labs

object FocusPasc {
  def main(args: Array[String]) {
    val n = readLine().toString.toInt

    var x: Long = 1
    for (i <- 0 to n) {
      print(s"$x ")
      x = x * (n - i) / (i + 1)
    }
  }
}