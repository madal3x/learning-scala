import rx.lang.scala._


// When an Observable object produces an exception, it enters the error state and cannot emit more events.
val exc = new RuntimeException
val o = Observable.items(1, 2) ++ Observable.error(exc)
o.subscribe(
  x => println(s"number $x"),
  t => println(s"an error occurred: $t")
)


val classics = List("Good, bad, ugly", "Titanic", "Die Hard")
val movies = Observable.from(classics)
movies.subscribe(new Observer[String] {
  override def onNext(m: String) = println(s"Movies Watchlist - $m")
  override def onError(e: Throwable) = println(s"Ooops - $e!")
  override def onCompleted() = println(s"No more movies.")
})
