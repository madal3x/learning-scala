import org.apache.commons.io.monitor._
import rx.lang.scala._


// Use the Observable.create factory method to create the Observable objects from callback-based APIs.
def modified(directory: String): Observable[String] = {
  Observable { observer =>
    val fileMonitor = new FileAlterationMonitor(1000)
    val fileObs = new FileAlterationObserver(directory)
    val fileLis = new FileAlterationListenerAdaptor {
      override def onFileChange(file: java.io.File) {
        observer.onNext(file.getName)
      }
    }
    fileObs.addListener(fileLis)
    fileMonitor.addObserver(fileObs)
    fileMonitor.start()

    //Implementations of the unsubscribe method in the Subscription trait need to be idempotent.
    //Use the Subscription.apply method to create the Subscription objects that are idempotent by default.
    Subscription { fileMonitor.stop() }
  }
}


println(s"starting to monitor files")
val sub = modified(".").subscribe(n => println(s"$n modified!"))
println(s"please modify and save a file")
Thread.sleep(10000)
sub.unsubscribe()
println(s"monitoring done")


val fileMonitor = new FileAlterationMonitor(1000)
fileMonitor.start()

def hotModified(directory: String): Observable[String] = {
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
