package concprog.rxscala.subject

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala._
import rx.lang.scala.subjects.ReplaySubject

object Replay extends App with LazyLogging {
  val messageBus = Subject[String]()
  // buffers all events and replays them to a subscriber on subscription
  val messageLog = ReplaySubject[String]()
  messageBus.subscribe(logger.info(_))
  messageBus.subscribe(messageLog)
}
