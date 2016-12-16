package concprog.rxscala

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala.Observable
import scala.concurrent.duration._

object Compose extends App with LazyLogging{
  val odds = Observable
    .interval(100 millis)
    .filter(_ % 2 == 1)
    .map(n => s"num $n")
    .take(5)
  odds.subscribe(
    println,
    e => logger.info(s"unexpected $e"),
    () => logger.info("no more odds"))

  Thread.sleep(1000)


  val evens = for {
    n <- Observable.from(0 until 9)
    if n % 2 == 0
  } yield s"even number $n"

  evens.subscribe(logger.info(_))
}
