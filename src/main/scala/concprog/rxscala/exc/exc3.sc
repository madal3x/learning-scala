import rx.lang.scala.{Observable, Subscription}

import scala.concurrent.duration._
import scala.util.Random

def randomString(length: Int, l: List[Char] = List.empty[Char]):List[Char] = {
  if (length == 1) util.Random.nextPrintableChar :: l
  else randomString(length-1,util.Random.nextPrintableChar :: l)
}

def randomQuote = Observable
  .interval(100.millis)
  .map(e => randomString(Random.nextInt(10)+1))

randomQuote.scan((0D, 0)) {
  (n, s) => n match {
    case (l, c) => (l + s.length, c + 1)
  }
}
  .tail
  .map(e => e._1 / e._2)
  .subscribe(println(_))

Thread.sleep(5000)
