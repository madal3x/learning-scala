package concprog.rxscala

import rx.lang.scala.Observable
import scala.concurrent.duration._

object Back extends App{
  val o = Observable.interval(100.millis)

  def slow(l: Long) = {
    Thread.sleep(500)
    println(s"slow $l")
  }

  def fast(l: Long) = {
    println(s"fast $l")
  }

  o.subscribe(slow(_))
  o.subscribe(fast(_))

  Thread.sleep(5000)
}
