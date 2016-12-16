package concprog.rxscala

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala._

object SchedulersComputation extends App with LazyLogging{
  val scheduler = schedulers.ComputationScheduler()
  val numbers = Observable.from(0 until 20)
  numbers.subscribe(n => logger.info(s"num $n"))
  // observeOn returns a new Observable that will emit on the specified scheduler (to force emitting on a specific thread)
  numbers.observeOn(scheduler).subscribe(n => logger.info(s"num $n"))
  Thread.sleep(2000)
}
