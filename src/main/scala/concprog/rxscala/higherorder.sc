import scala.concurrent.{Future, blocking}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import rx.lang.scala._

def fetchQuote(): Future[String] = Future {
  blocking {
    val url = "http://www.iheartquotes.com/api/v1/random?" +
      "show_permalink=false&show_source=false"
    Source.fromURL(url).getLines.mkString
  }
}

// cold observable, multicasts the future value to all observers
def fetchQuoteObservable(): Observable[String] = {
  Observable.from(fetchQuote())
}

// concat preserves firing timing
// flatten returns in the order they are received, not preserving initial ordering
def quotes: Observable[Observable[String]] =
  Observable
    .interval(0.5 seconds)
    .take(4)
    .map { n =>
      fetchQuoteObservable()
        .map(txt => s"$n) $txt")
    }

// Use concat to flatten nested Observable objects whenever the order of events between different nested Observable objects needs to be maintained.
// If at least one of the nested Observable objects has an unbounded number of events or never completes, use flatten instead of concat.

println(s"Using concat")
quotes.concat.subscribe(println(_))
Thread.sleep(6000)
println(s"Now using flatten")
quotes.flatten.subscribe(println(_))
Thread.sleep(6000)

// flatMap and for return in order events are received
// with flatMap
Observable
  .interval(0.5 seconds)
  .take(5)
  .flatMap({ n =>
    fetchQuoteObservable().map(txt => s"$n) $txt")
  })
  .subscribe(println(_))

// with for
val qs = for {
  n <- Observable.interval(0.5 seconds).take(5)
  txt <- fetchQuoteObservable()
} yield s"$n) $txt"
qs.subscribe(println(_))

