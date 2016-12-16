package concprog.rxscala.subject

import rx.lang.scala.{Observable, Subscription}
import org.apache.commons.io.monitor._

object FileSystemModule {
  private val fileMonitor = new FileAlterationMonitor(1000)
  fileMonitor.start()

  val fileModifications = modified(".")

  private def modified(directory: String): Observable[String] = {
    val fileObs = new FileAlterationObserver(directory)
    fileMonitor.addObserver(fileObs)
    Observable { observer =>
      val fileLis = new FileAlterationListenerAdaptor {
        override def onFileChange(file: java.io.File) {
          observer.onNext(file.getName)
        }
      }
      fileObs.addListener(fileLis)
      Subscription { fileObs.removeListener(fileLis) }
    }
  }
}
