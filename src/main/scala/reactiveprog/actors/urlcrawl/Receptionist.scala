package reactiveprog.actors.urlcrawl

import akka.actor.{Actor, ActorRef, Props, SupervisorStrategy, Terminated}
import akka.dispatch.sysmsg.Failed

object Receptionist {
  case class Get(url: String)
  case class Result(url: String, links: Set[String])
  case class Failed(url: String)
}

class Receptionist extends Actor {

  import Receptionist._

  override def supervisorStrategy = SupervisorStrategy.stoppingStrategy

  val QueueLimit = 3
  val MaxCrawlDepth = 2

  case class Job(client: ActorRef, url: String)

  var reqNo = 0

  def runNext(queue: Vector[Job]): Receive = {
    reqNo += 1
    if (queue.isEmpty) waiting
    else {
      val controller = context.actorOf(Props[Controller], s"Controller$reqNo")
      context.watch(controller)
      controller ! Controller.Check(queue.head.url, MaxCrawlDepth)
      running(queue)
    }
  }

  def enqueueJob(queue: Vector[Job], job: Job): Receive = {
    if (queue.size > QueueLimit) {
      sender() ! Failed(job.url)
      running(queue)
    } else
      running(queue :+ job)
  }

  def receive = waiting

  // on Get(url) start traversal and become running
  val waiting: Receive = {
    case Get(url) => context.become(runNext(Vector(Job(sender(), url))))
  }

  // on Get(url) append that to queue and keep running
  // upon Controller.Result(links) ship that to client
  // and run next job from queue (if any)
  def running(queue: Vector[Job]): Receive = {
    case Controller.Result(links) =>
      val job = queue.head
      job.client ! Result(job.url, links)
      /*context.stop(sender())*/
      context.stop(context.unwatch(sender()))
      context.become(runNext(queue.tail))

    // we are not interested in the reference to the ActorRef that was terminated
    // because at any point in time there can be only one
    case Terminated(_) =>
      val job = queue.head
      job.client ! Failed(job.url)
      context.become(runNext(queue.tail))

    case Get(url) =>
      context.become(enqueueJob(queue, Job(sender(), url)))
  }
}