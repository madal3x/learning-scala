package reactiveprog.actors.urlcrawl

import org.jsoup.Jsoup
import scala.collection.JavaConversions._
import scala.concurrent.Future

object LinkFinder {
  def findLinks(body: String, url: String): Iterator[String] = {
    val d = Jsoup.parse(body, url)
    val ls = d.select("a[href]")
    for {
      l <- ls.iterator()
    } yield l.absUrl("href")
  }
}