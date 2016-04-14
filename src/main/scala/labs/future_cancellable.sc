import java.util.concurrent.CancellationException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

type Cancellable[T] = (Promise[Unit], Future[T])

def cancellable[T](body: Future[Unit] => T): Cancellable[T] = {
  val cancel = Promise[Unit]
  val f = Future {
    val r = body(cancel.future)

    /** if tryFailure returns false then the client must have already completed the cancel promise
      * in this case we cannot fail the client's attempt to cancel the computation so we throw CancellationException
      * this exists to avoid the race in which the client successfully requests the cancellation and the future computation simultaneously completes the future
      */
    if (!cancel.tryFailure(new Exception))
      throw new CancellationException

    r
  }

  (cancel, f)
}

val (cancel, value) = cancellable { cancel =>
  var i = 0
  while (i < 5) {
    if (cancel.isCompleted)
      throw new CancellationException

    Thread.sleep(500)

    println(s"$i: working")

    i += 1
  }

  "resulting value"
}

Thread.sleep(1500)
cancel trySuccess ()
println("computation cancelled")
Thread.sleep(2000)