package reactiveprog.actors.urlcrawl

import akka.actor.{ActorRef, Actor}
import akka.pattern.pipe

class Cache extends Actor {
  case class Get(url: String)
  case class Result(client: ActorRef, url: String, body: String)

  var cache = Map.empty[String, String]

  def receive = {
    case Get(url) =>
      if (cache contains url)
        sender ! cache(url)
      else {
        import scala.concurrent.ExecutionContext.Implicits.global

        // cache client because sender might change till the future gets executed
        val client = sender()
        WebClient.get(url).map(Result(client, url, _)).pipeTo(client)
      }

    case Result(client, url, body) =>
      cache += url -> body
      client ! body
  }
}
