import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

val p = Promise[String]
val q = Promise[String]
p.future foreach { x =>
  println(s"p succeeded with $x")
}
p success "assigned"
q failure new Exception("not kept")
q.future.failed foreach { t =>
  println(s"q failed with $t")
}


def myFuture[T](b: => T): Future[T] = {
  val p = Promise[T]

  global.execute(new Runnable {
    def run(): Unit = try {
      p.success(b)
    } catch {
      case NonFatal(e) =>
        p.failure(e)
    }
  })

  p.future
}

val f = myFuture{ "naa" + "na" * 8}
f.foreach(t => println(t))