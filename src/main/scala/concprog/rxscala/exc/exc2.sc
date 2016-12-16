// http://shop.oreilly.com/product/9781783281411.do

import rx.lang.scala._
import scala.concurrent.duration._

val o1 = Observable
  .interval(1.second)
  .filter(e => (e % 5 == 0 || e % 12 == 0) && !(e % 30 == 0))

o1.subscribe(println(_))


val o2a = Observable.interval(5.seconds).map(_ * 5)
val o2b = Observable.interval(12.seconds).map(_ * 12)

val o2 = (o2a merge o2b distinct) filter (_ % 30 != 0)
