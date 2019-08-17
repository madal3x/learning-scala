package labs

object FocusFib {
  def main(args: Array[String]) {
    val n = readLine().toString.toInt
    (1 to n)
      .map(_ => readLine())
      .map(_.toString.toLong)
      .foreach(nr =>
        println(nextfib(nr)))

    def nextfib(n: Long): Long = {
      @annotation.tailrec
      def loop(a: Int, b: Int): Long = {
        if (n < a) a
        else loop(b, a + b)
      }

      loop(0, 1)
    }
  }
}