package reactiveprog.actors.counter

import akka.actor.{Actor, Props}

class Counter extends Actor {
  var count = 0

  def receive = {
    case "incr" => count += 1
    case "get" => sender ! count
  }
}

class CounterStateful extends Actor {
  def counter(n: Int): Receive = {
    case "incr" => context.become(counter(n + 1))
    case "get" => sender ! n
  }

  def receive = counter(0)
}

class Main extends Actor {
  val counter = context.actorOf(Props[Counter], "counter")

  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"

  def receive = {
    case count: Int =>
      println(s"count was $count")
      context.stop(self)
  }
}