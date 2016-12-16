package concprog.rxscala.subject

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala.Observable.interval

import scala.concurrent.duration._

object TimeModule {
  val systemClock = interval(1.second).map(t => s"systime: $t")
}
