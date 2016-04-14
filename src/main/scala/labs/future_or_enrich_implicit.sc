import scala.concurrent.{Future, Promise}

implicit class FutureOps[T](val thisFuture: Future[T]) {
  // or is non-deterministic
  def or(thatFuture: Future[T]): Future[T] = {
    val p = Promise[T]
    thisFuture onComplete { x => p tryComplete x }
    thatFuture onComplete { y => p tryComplete y }

    p.future
  }
}

