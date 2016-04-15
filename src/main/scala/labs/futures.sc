import scala.concurrent.Future
import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

// - the order of executing future callbacks is not guaranteed, and also no guarantee for the thread they will be run on
// - registering a callback on the future which is already completed will eventually execute the callback
// - if some of the callbacks throw an exception the other callbacks will be executed regardless
// - once executed, the callbacks are removed from the future object, thus being eligible for GC
// the ExecutionContext is a threadpool, there's a global implicit implementation
val f = Future {
  List(1, 2, 3, 4, 5, 6)
}
f onFailure {
  case npe: NullPointerException =>
    println("I'd be amazed if this printed out.")
}

val firstOccurrence: Future[Int] = Future {
  val source = scala.io.Source.fromFile("myText.txt")
  source.toSeq.indexOfSlice("myKeyword")
}
f onComplete {
  case Success(posts) => for (post <- posts) println(post)
  case Failure(t) => println("An error has occured: " + t.getMessage)
}
firstOccurrence onSuccess {
  case idx => println("The keyword first appears at position: " + idx)
}
firstOccurrence onFailure {
  case t => println("Could not process file: " + t.getMessage)
}

// - the callbacks can also execute concurrently, in which case the totalA could be wrong at the end
// - @volatile means that the variable is modified by multiple threads,
// no thread-local cache, synchronization on access, atomic get-set
// http://www.javamex.com/tutorials/synchronization_volatile.shtml
@volatile var totalA = 0
val text = Future {
  "a" * 16 + "BATMAN!!!"
}
text onSuccess {
  case txt => totalA += txt.count(_ == 'a')
}
text onSuccess {
  case txt => totalA += txt.count(_ == 'A')
}

// - if some of the callbacks never complete the other callbacks may not be executed at all
// for this a blocking construct should be used