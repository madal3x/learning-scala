import rx.lang.scala._

import scala.io.Source

// hot observable, unicast, for every subscriber a new quote
def randomQuote = Observable[String] { obs =>
  val url = "http://www.iheartquotes.com/api/v1/random?" +
    "show_permalink=false&show_source=false"
  obs.onNext(Source.fromURL(url).getLines.mkString)
  obs.onCompleted()
  Subscription()
}

object CompositionRetry extends App {
  import Observable._
  def errorMessage = items("Retrying...") ++ error(new Exception)
  def quoteMessage = for {
    text    <- randomQuote
    // if text is longer than 100 chars, emit a "Retrying..." followed by an error
    message <- if (text.size < 100) items(text) else errorMessage
  } yield message
  // retry resubscribes to an Observable when it fails
  quoteMessage.retry(5).subscribe(println(_))
  Thread.sleep(2500)
}

// how many quotes are on average longer than 100chars?
object CompositionScan extends App {
  // repeat resubscribes to an Observable when it completes
  CompositionRetry.quoteMessage.retry.repeat.take(100).scan(0) {
    (n, q) => if (q == "Retrying...") n + 1 else n
  } subscribe(n => println(s"$n / 100"))
}


