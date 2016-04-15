package reactiveprog.actors.urlcrawl

import akka.actor.{Status, Actor}
import akka.pattern.pipe

import scala.util.{Failure, Success}

object Getter {
  /*case object Done*/
  case object Abort
}

class Getter(url: String, depth: Int) extends Actor {

  import Getter._

  implicit val exec = context.dispatcher

  WebClient get url pipeTo self

  def receive = {
    case body: String =>
      for (link <- LinkFinder.findLinks(body, url))
        context.parent ! Controller.Check(link, depth)
      context.stop(self)

    case _: Status.Failure => context.stop(self)

    case Abort => context.stop(self)
  }

  // use Terminated DeathWatch instead of a special message like Done
  /*def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }*/
}