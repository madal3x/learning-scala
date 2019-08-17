import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

def fib(n : BigInt) : Future[BigInt] = {
  if(n <= 1) Future.successful(1)
  else {
    for {
      a <- fib(n-1)
      b <- fib(n-2)
    } yield a + b
  }
}


def fib2(n : BigInt) : BigInt = {
  if(n <= 1) 1
  else fib2(n-1) + fib2(n-2)
}

val n : Int = 20
//val fib3 = Future {fib2(n)}

val result = Await.result(fib(n), 500.millis)
println(result)