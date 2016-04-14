import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

// Happy Future
val happyFuture = Future {
  42
}

// Bleak future
val bleakFuture = Future {
  throw new Exception("Mass extinction!")
}

// We would want to wrap the result into a hypothetical http response
case class Response(code: Int, body: String)

// This is the handler we will use
def handle[T](future: Future[T]): Future[Response] = {
  future.map {
    case answer: Int => Response(200, answer.toString)
  } recover {
    case t: Throwable => Response(500, "Uh oh!")
  }
}

val resultHappy = Await.result(handle(happyFuture), 1 second)
println(resultHappy)


val resultBleak = Await.result(handle(bleakFuture), 1 second)
println(resultBleak)
