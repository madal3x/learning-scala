import scalaz._
import Scalaz._
import Kleisli._
import scalaz.concurrent.Task
import delorean._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


def f1(x: Int): Task[String] = {
  Task((1 to x).map(_ => "hello").mkString(" "))
}

def f2(s: String): Task[List[String]] = {
  Task(s.split(" ").toList)
}

def composeFlatMap(x: Int): Task[List[String]] = {
  for {
    r1 <- f1(x)
    r2 <- f2(r1)
  } yield r2
}

val composeKleisli: Kleisli[Task, Int, List[String]] =
  kleisli(f1) >=> kleisli(f2)

val t: Task[List[String]] = composeKleisli(3)
def f: Future[List[String]] = t.unsafeToFuture()

Await.result(f, 100.millis)