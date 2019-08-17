package labs

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object MyStream extends App {
  val overflowStrategy = akka.stream.OverflowStrategy.backpressure

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import scala.concurrent.ExecutionContext.Implicits.global

  val source =
    Source
      .queue[Int](20, overflowStrategy)
      .groupBy(2, _ % 2 == 0)
      .groupedWithin(5, 5 seconds)
      .map{seq => println("seq: " + seq); seq}
      .mapAsyncUnordered(3) {
        case ns if ns.head % 2 == 0 =>
          multiply(ns, 100).map(mns => ns zip mns)
        case ns =>
          multiply(ns, 1000).map(mns => ns zip mns)
      }
      .mapConcat(identity)
      .mergeSubstreams

  //!!!treat the case with multiple requests same query
  // have a statefulFlow and discard duplicates, which should be emptied after a certain count (or time?)
  def exec(ns: Seq[Int]): Future[Seq[(Int, Int)]] = {
    println("exec: " + ns)

    var rc = 0

    val countReceivedFlow = Flow[(Int, Int)].map(pair => {if (ns.contains(pair._1)) rc+=1; println(pair); pair})

    val foldSink = Sink.fold[Seq[(Int, Int)], (Int, Int)](Seq())((seq, pair) => pair +: seq)

    val queue =
      source
        .completionTimeout(10 seconds)
        .filter(pair => ns.contains(pair._1))
        .via(countReceivedFlow)
        .takeWhile(_ => rc != ns.length, inclusive = true)
        .toMat(foldSink)(Keep.both)
        //.async
        .run()

    ns.foreach(n => queue._1.offer(n))

    queue._2
  }


  val r1 = exec(Seq(0,2,4,6,8))
  val r2 = exec(Seq(10,11,13,15,17,19,21))
  val r3 = exec(Seq(22,23,24))

  r1.onSuccess{case s => println("r1: " + s)}

  //Thread.sleep(3000)

  r2.onSuccess{case s => println("r2: " + s)}

  //Thread.sleep(3000)

  r3.onSuccess{case s => println("r3: " + s)}

  Await.result(r1, 15 seconds)

  Await.result(r2, 15 seconds)

  Await.result(r3, 15 seconds)

  Await.ready(system.terminate(), 5 seconds)

  def multiply(ns: Seq[Int], multiplier: Int) = Future {
    ns.map(_ * multiplier)
  }
}
