import scala.concurrent.Future

val f1 = Future {"f1"}
val f2 = Future {2}

val a: Future[Future[Int]] = f1.map(v => f2)
val b: Future[Int] = f1.flatMap(v => f2)

// f1 and f2 execute in parallel, and they combine into another future
val b2: Future[Int] = for {
  f11 <- f1
  f22 <- f2
} yield f22

// f1 and f2 are executed sequentially, but still async, not blocking the main thread
val c2: Future[Int] = for {
  f111 <- Future {"f1"}
  f222 <- Future {2}
} yield f222