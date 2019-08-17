package labs

object FocusSeq {
  def main(args: Array[String]) {
    val n = readLine().toString.toInt
    val seq = readLine.toString.split(" ").map(_.toInt).toList

    var maxlen = 1
    for (i <- 0 until n - 1) {
      var curlen = 1
      var prev = seq(i)

      for (j <- i + 1 until n) {
        if (seq(j) > prev) {
          curlen += 1
          prev = seq(j)
        }
      }

      if (curlen > maxlen) maxlen = curlen
    }

    println(maxlen)
  }
}