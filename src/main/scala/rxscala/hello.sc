import rx.lang.scala._
import rx.schedulers.Schedulers

import scala.concurrent.Future
import scala.concurrent.duration._

object hello {
  val o = Observable[String] { s =>
    s.onNext("Hello")
    s.onNext("World!")
    s.onCompleted()
  }

  o.subscribe(println(_))

  Observable
    .just("Hello", "World!")
    .subscribe(println(_))

  Observable
    .just("Hello", "World!")
    .subscribe(println(_),
      _.printStackTrace(),
      () => println("Done!"))

  Observable[String] { s =>
    try {
      s.onNext("Hello World!")
      s.onCompleted()
    } catch {
      case e: Exception =>
        s.onError(e)
    }
  }.subscribe(println(_))

  List("red", "green", "blue")
    .toObservable
    .delay(40 millis)
    .subscribe(new Subscriber[String] {
      override def onNext(value: String): Unit =
        println(value)
      override def onError(error: Throwable): Unit =
        error.printStackTrace()
      override def onCompleted(): Unit =
        println("Done")
    })

  def hello(names: String*) {
    Observable.from(names) subscribe { n =>
      println(s"Hello $n!")
    }
  }

  val slow = Observable.interval(40 millis).take(5).map("slow " + _)
  val fast = Observable.interval(20 millis).take(10).map("fast " + _)
  val o3 = slow merge fast
  o3.subscribe(println(_))
  waitFor(o3)

  val o2 = Observable.from(List("red", "green", "blue"))
    .concatMap(s => Observable.from(fut(s)))
    .delay(30 millis)
    .doOnSubscribe(println("subscribed"))
    .doOnUnsubscribe(println("unsubscribed"))
    .doOnCompleted(println("completed"))
    .doOnTerminate(println("terminated"))

  def fut(s: String): Future[String] = Future.successful(s)

  o2.subscribe(println(_), e => e.printStackTrace())

  Observable[String] { subscriber =>
    new Thread(new Runnable {
      override def run(): Unit = {
        for (i <- 0 to 75) {
          if (subscriber.isUnsubscribed)
            return

          subscriber.onNext("value " + i)
        }

        if (!subscriber.isUnsubscribed)
          subscriber.onCompleted()
      }
    })
  }.subscribe(println(_))

  Observable[Int] { subscriber =>
    try {
      subscriber.onNext(getData)
      subscriber.onCompleted()
    } catch {
      case e: Exception =>
        subscriber.onError(e)
    }
  }
    .subscribeOn(rx.lang.scala.schedulers.IOScheduler())

  def getData = 1


  def waitFor[T](obs: Observable[T]): Unit = {
    obs.toBlocking.toIterable.last
  }
}