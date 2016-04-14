package reactiveprog.actors.urlcrawl

import java.net.URL

import akka.actor._
import scala.concurrent.duration._
import scala.util.Try

object Controller {
  case class Check(url: String, depth: Int)
  case class Result(links: Set[String])
}

class Controller extends Actor with ActorLogging {
  import Controller._

  // the receive timeout is reset by every received message, after which it will shutdown itself
  context.setReceiveTimeout(10.seconds)

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5) {
    case _: Exception => SupervisorStrategy.Restart
  }

  /*var children = Set.empty[ActorRef]*/

  // prefer variables to mutable structures as they can be safely shared
  var cache = Set.empty[String]
  def receive = {
    case Check(url, depth) =>
      log.debug("{} checking {}", depth, url)
      if (!isValidUrl(url)) {
        log.debug("{} invalid url {}", depth, url)
      } else if (!cache(url) && depth > 0) {
        val child = context.actorOf(Props(new Getter(url, depth - 1)))
        context.watch(child)

        cache += url
      }

    case Terminated(_) =>
      if (context.children.isEmpty)
        context.parent ! Result(cache)

    case ReceiveTimeout =>
      context.children foreach context.stop

    /*case Getter.Done =>
      children -= sender
      if (children.isEmpty) context.parent ! Result(cache)*/

    /*case ReceiveTimeout => children foreach (_ ! Getter.Abort)*/
  }

  private def isValidUrl(url: String): Boolean = {
    Try{new URL(url)}.isSuccess && url != "https://itunes.apple.com:443"
  }
}