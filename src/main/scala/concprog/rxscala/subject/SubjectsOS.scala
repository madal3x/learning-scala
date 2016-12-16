package concprog.rxscala.subject

import com.typesafe.scalalogging.LazyLogging

object SubjectsOS extends App with LazyLogging{
  logger.info("boot starting")
  val loadedModules = List(
    TimeModule.systemClock,
    FileSystemModule.fileModifications
  ).map(_.subscribe(RxOS.messageBus))
  logger.info("boot finished")
  Thread.sleep(10000)
  for (m <- loadedModules) m.unsubscribe()
  logger.info(s"RxOS dumping the complete system event log")
  RxOS.messageLog.subscribe(logToFile(_))
  logger.info("shutdown")

  def logToFile(s: String) = logger.info(s"log $s")
}
