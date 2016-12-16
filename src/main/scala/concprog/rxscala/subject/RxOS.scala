package concprog.rxscala.subject

import com.typesafe.scalalogging.LazyLogging
import concprog.rxscala.subject.Replay.logger
import rx.lang.scala.subjects.ReplaySubject
import rx.lang.scala.{Observable, Subject}

object RxOS extends LazyLogging{
  val messageBusWrong = Observable.items(
    TimeModule.systemClock,
    FileSystemModule.fileModifications
  ).flatten.subscribe(logger.info(_))

  val messageBus = Subject[String]

  // buffers all events and replays them to a subscriber on subscription
  val messageLog = ReplaySubject[String]()
  messageBus.subscribe(logger.info(_))
  messageBus.subscribe(messageLog)
}
