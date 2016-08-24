import akka.NotUsed
import akka.stream.{ActorAttributes, Supervision}
import akka.stream.scaladsl.{Flow, Source}

import scala.concurrent.Future

def lookupEmail(handle: String): Future[String] = ???

case class Author(name: String)

val authors = Source(List(Author("auth1"), Author("auth2"), Author("auth3")))

val emailAddresses: Source[String, NotUsed] =
  authors.via(
    Flow[Author]
      .mapAsync(4)(author => lookupEmail(author.name))
      .withAttributes(ActorAttributes.supervisionStrategy(Supervision.resumingDecider)))

